package org.cotrix.repository.impl;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.trait.Identified;
import org.cotrix.repository.Query;
import org.cotrix.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//base delegate implementation based on public API
//abstract because subclasses required to instantiate parameter and make themselves available for injection
//validates inputs and delegates to persistence-specific implementations based on private API 

public abstract class AbstractRepository<T extends Identified, P extends Identified.Abstract<? extends T,?>> implements Repository<T>  {

	//P is the private implementation. Java does not let us say directly that
	//but we get close by requiring with the wildcard on the self type parameter Identified.Abstract<? extends T,?>)
	
	private static Logger log = LoggerFactory.getLogger(Repository.class);
	
	private final Repository<P> delegate;
	
	public AbstractRepository(Repository<P> repository) {
		
		notNull("delegate",repository);
		
		this.delegate=repository;
		
	}
	
	@Override
	public void add(T entity) {
		
		notNull("entity",entity);

		P implementation = reveal(entity);
		
		if (implementation.isChangeset())
			throw new IllegalArgumentException("entity "+entity.id()+" is a changeset and cannot be added");
		
		delegate.add(implementation);
		
		log.trace("added entity {}",entity.id());
	};
	
	@Override
	public <U> U get(Query<T,U> query) {
	
		notNull("query", query);
		
		return delegate.get(reveal(query));
	}
	
	@Override
	public int size() {
		return delegate.size();
	}
	
	public void update(T changeset) {
		
		notNull("changeset",changeset);
		
		P implementation = reveal(changeset);

		if (!implementation.isChangeset())
			throw new IllegalArgumentException(implementation.id()+" is not a changeset");
		
		delegate.update(implementation);
	};
	
	@Override
	public void remove(String id) {
		
		valid("entity identifier",id);
		
		delegate.remove(id);	
	}
	
	@Override
	public T lookup(String id) {
		
		valid("entity identifier",id);
		
		return reveal(delegate.lookup(id));
	}

	//helpers
	
	@SuppressWarnings("all")
	private <R> Query<P,R> reveal(Query<T,R> query) {
		return (Query<P,R>) query;
	}
	
	@SuppressWarnings("all")
	public <S,T> S reveal(T entity) {
		return (S) entity;
	}
}
