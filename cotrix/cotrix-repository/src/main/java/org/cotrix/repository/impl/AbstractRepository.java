package org.cotrix.repository.impl;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.repository.Query;
import org.cotrix.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//base delegate implementation based on public API
//abstract because subclasses required to instantiate parameter and make themselves available for injection
//validates inputs and delegates to persistence-specific implementations based on private API 

public abstract class AbstractRepository<T extends Identified, 
										 P extends Identified.Abstract<? extends T,S>,
										 S extends Identified.State & EntityProvider<P>> 
										  
										implements Repository<T>
{

	//T is the public entity type
	//P is the private entity type. Java generic dont let us say it explicitly here, but mistakes are still detected
	//S is the state of private entities
	
	private static Logger log = LoggerFactory.getLogger(Repository.class);
	
	private final StateRepository<S> delegate;
	
	
	public AbstractRepository(StateRepository<S> repository) {
		
		notNull("delegate repository",repository);
		
		this.delegate=repository;
		
	}
	
	
	@Override
	public void add(T entity) {
		
		notNull("entity",entity);

		P implementation = retype(entity);
		
		if (implementation.isChangeset())
			throw new IllegalArgumentException("entity "+entity.id()+" is a changeset and cannot be added");
		
		if (delegate.contains(implementation.id()))
			throw new IllegalArgumentException("entity "+entity.id()+" is already in this repository");
		
		delegate.add(implementation.state());
		
		log.trace("added entity {}",entity.id());
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
		
		return delegate.get(retype(query));
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
			throw new IllegalArgumentException(implementation.id()+" is not a changeset");
		
		S state = delegate.lookup(implementation.id());
		
		if (state==null)
			throw new IllegalStateException(implementation.id()+" is not in this repository, hence cannot be updated.");
			
		Identified.Abstract entity = state.entity();
		
		entity.update(implementation);
	};
	
	
	@Override
	public void remove(String id) {
		
		valid("entity identifier",id);
		
		if (!delegate.contains(id))
			throw new IllegalStateException("entity "+id+" is not in this repository, hence cannot be removed.");
		
		delegate.remove(id);	
	}
	

	//helpers
	
	//hide ugly casts to convince compiler P is a T and T can only be P
	//pragmatically safe as P is sole implementation of T;
	
	@SuppressWarnings("all")
	private <R> Query<S,R> retype(Query<T,R> query) {
		return (Query) query;
	}
	
	@SuppressWarnings("all")
	public <A,B> A retype(B entity) {
		return (A) entity;
	}
}
