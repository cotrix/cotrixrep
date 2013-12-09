package org.cotrix.repository.impl;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.repository.Query;
import org.cotrix.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// base delegate implementation based on public API
// abstract because subclasses required to instantiate parameter and make themselves available for injection
// validates inputs and delegates to persistence-specific implementations based on private API

public abstract class AbstractRepository<T extends Identified, 
										 P extends Identified.Abstract<? extends T,S>, 
										 S extends Identified.State & EntityProvider<? extends P> >
		
										implements Repository<T> 

	{

	//on parameters:
	// T is the public entity type
	// P is the private entity type. Java generics dont let us say directly that, but it's accident-proof enough;
	// S is the state of the private entity type, again not super tight but accident-proof;

	
	private static Logger log = LoggerFactory.getLogger(Repository.class);

	private final Repository<S> delegate;

	public AbstractRepository(Repository<S> repository) {

		notNull("delegate", repository);

		this.delegate = repository;

	}

	@Override
	public void add(T entity) {

		notNull("entity", entity);

		P implementation = retype(entity);

		if (implementation.isChangeset())
			throw new IllegalArgumentException("entity " + entity.id() + " is a changeset and cannot be added");

		delegate.add(implementation.state());

		log.trace("added entity {}", entity.id());
	};

	@Override
	public <U> U get(Query<T, U> query) {

		notNull("query", query);

		return delegate.get(retype(query));
	}

	@Override
	public int size() {
		return delegate.size();
	}

	
	@Override
	@SuppressWarnings("all")
	public void update(T changeset) {

		notNull("changeset", changeset);

		P implementation = retype(changeset);

		if (!implementation.isChangeset())
			throw new IllegalArgumentException(implementation.id() + " is not a changeset");

		P entity = delegate.lookup(changeset.id()).entity();
		
		((Identified.Abstract) entity).update(implementation);
	};

	@Override
	public void remove(String id) {

		valid("entity identifier", id);

		delegate.remove(id);
	}

	@Override
	public T lookup(String id) {

		valid("entity identifier", id);

		return retype(delegate.lookup(id));
	}

	// helpers
	
	
	// just dressings over ugly casts to pass from P to T and vice-versa.
	// circumstances make this safe enough:
	// a) we work with a single implementation of T, can hardly pass another by mistake;
	// b) we know P is that implementation, can hardly abuse class parametrisation by mistake;
		
	
	@SuppressWarnings("all")
	private <R> Query<S, R> retype(Query<T, R> query) {
		return (Query<S, R>) query;
	}

	@SuppressWarnings("all")
	public <A, B> A retype(B entity) {
		return (A) entity;
	}
}
