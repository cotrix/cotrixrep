package org.cotrix.repository.impl;

import static java.lang.System.*;
import static org.cotrix.common.Utils.*;

import org.cotrix.common.Utils;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.repository.Query;
import org.cotrix.repository.Repository;
import org.cotrix.repository.UpdateAction;
import org.cotrix.repository.spi.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//base delegate implementation based on public API
//abstract because subclasses required to instantiate parameter and make themselves available for injection
//validates inputs and delegates to persistence-specific implementations based on private API 

public abstract class AbstractRepository<T extends Identified, 
										 P extends Identified.Abstract<P,S>,
										 S extends Identified.State & EntityProvider<P>> 
										  
										implements Repository<T>
{

	//T is the public entity type
	//P is the private entity type. Java generic dont let us say it explicitly here, but mistakes are still detected
	//S is the state of private entities
	
	private static Logger log = LoggerFactory.getLogger(Repository.class);
	
	private final StateRepository<S> delegate;
	
	private final EventProducer producer;
	
	
	public AbstractRepository(StateRepository<S> repository, EventProducer producer) {
		
		notNull("delegate repository",repository);
		notNull("event producer",producer);
		
		this.delegate=repository;
		this.producer=producer;
		
	}
	
	
	@Override
	public void add(T entity) {
		
		notNull("entity",entity);

		P implementation = retype(entity);
		
		if (implementation.isChangeset())
			throw new IllegalArgumentException("entity "+log(entity)+"is a changeset and cannot be added");
		
		if (delegate.contains(implementation.id()))
			throw new IllegalArgumentException("entity "+log(entity)+"is already in this repository");
		
		long time = currentTimeMillis();
		
		delegate.add(implementation.state());
		
		log.trace("added entity {} in {} ms.",log(entity),currentTimeMillis()-time);
		
		producer.additions.fire(entity);
		
	};
	
	
	@Override
	public T lookup(String id) {
		
		valid("entity identifier",id);
		
		S state = delegate.lookup(id);
		
		if (state==null)
			return null ;
		else 
			return retype(state.entity());
	}

	
	@Override
	public <R> R get(Query<T,R> query) {
	
		notNull("query", query);
		
		return reveal(query).execute();
	}
	
	@Override
	public int size() {
		return delegate.size();
	}
	
	@SuppressWarnings("all")
	public void update(T changeset) {
		
		notNull("changeset",changeset);
		
		Identified.Abstract implementation = retype(changeset);

		if (!implementation.isChangeset())
			throw new IllegalArgumentException(log(changeset)+"is not a changeset");
		
		S state = delegate.lookup(implementation.id());
		
		if (state==null)
			throw new IllegalStateException(log(changeset)+" is not in this repository, hence cannot be updated.");
			
		Identified.Abstract entity = state.entity();
		
		entity.update(implementation);
		
		producer.updates.fire(changeset);
	};
	
	
	@Override
	public void update(String id, UpdateAction<T> action) {
	
		notNull("codelist identifier",id);
		notNull("update action",action);
		
		T entity = lookup(id);
		
		if (entity==null)
			throw new IllegalStateException("entity "+id+" is not in this repository, hence cannot be updated.");
		
		long time = currentTimeMillis();
		
		try {
			action.performOver(entity);
		}
		catch(Exception e) {
			rethrow("cannot perform "+action+" (see cause) ",e);
		}
		
		log.trace("performed {} over {} in {} ms.",action,log(entity),currentTimeMillis()-time);
	}
	
	
	@Override
	public void remove(String id) {
		
		valid("entity identifier",id);
		
		if (!delegate.contains(id))
			throw new IllegalStateException("entity "+id+" is not in this repository, hence cannot be removed.");
		
		delegate.remove(id);	
		
		log.info("removed entity "+id);
		
		producer.removals.fire(id);

	}
	

	//helpers

	/* query for entity repository is also good for corresponding state repository */
	
	@SuppressWarnings("all")
	private <R> Query.Private<T,R> reveal(Query<T,R> query) {
		return Utils.reveal(query,Query.Private.class);
	}

	/* used to retype P as T and vice-versa.
	   it's pragmatically safe because P the sole implementation of T;*/

	@SuppressWarnings("all")
	public <A,B> A retype(B entity) {
		return (A) entity;
	}
	
	
	public String log(T entity) {
		return entity.id()+" ("+entity.getClass().getName()+") ";
	}
}

