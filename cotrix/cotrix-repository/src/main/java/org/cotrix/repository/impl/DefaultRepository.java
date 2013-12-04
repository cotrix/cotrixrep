package org.cotrix.repository.impl;

import static org.cotrix.common.Utils.*;

import org.cotrix.common.Utils;
import org.cotrix.domain.trait.Identified;
import org.cotrix.repository.Query;
import org.cotrix.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRepository<T extends Identified, S extends Identified.Abstract<S,?>, R extends Repository<S>> implements Repository<T>  {
	
	private static Logger log = LoggerFactory.getLogger(Repository.class);
	
	private final Class<T> publicType;
	private final Class<S> privateType;
	private final R repository;
	
	public DefaultRepository(R repository, Class<T> publicType, Class<S> privateType) {
		
		notNull("repository",repository);
		notNull("public publicType",publicType);
		notNull("public publicType",privateType);
		
		validateParameters(publicType,privateType); //compiler can't tell
				
		this.repository=repository;
		this.publicType=publicType;	
		this.privateType=privateType;
		
	}
	
	@Override
	public void add(T object) {
		
		notNull("element",object);

		S privateObject = reveal(object);
		
		if (privateObject.isChangeset())
			throw new IllegalArgumentException("this "+publicType.getCanonicalName()+" is a changeset and cannot be added");
		
		if (privateObject.id()!=null)
			throw new IllegalArgumentException(publicType.getCanonicalName()+" ("+privateObject.id()+") has been already persisted");
		
		repository.add(privateObject);
		
		log.trace("added {} ({})",publicType.getCanonicalName(),object.id());
	};
	
	@Override
	public <U> U get(Query<T,U> query) {
	
		notNull("query", query);
		
		return repository.get(reveal(query));
	}
	
	@Override
	public int size() {
		return repository.size();
	}
	
	public void update(T changeset) {
		
		notNull("changeset",changeset);
		
		S privateObject = reveal(changeset);

		if (!privateObject.isChangeset())
			throw new IllegalArgumentException(privateObject+" is not a changeset");
		
		if (privateObject.id()==null)
			throw new IllegalArgumentException(privateObject+" is not a changeset");
		
		repository.update(privateObject);
	};
	
	@Override
	public void remove(String id) {
		
		valid(publicType.getCanonicalName()+"'s identifier",id);
		
		repository.remove(id);	
	}
	
	@Override
	public T lookup(String id) {
		
		valid(publicType.getCanonicalName()+"'s identifier",id);
		
		return publicType.cast(repository.lookup(id));
	}

	//helpers
	
	private void validateParameters(Class<?> type, Class<?> privateType) {
		if (!type.isAssignableFrom(privateType))
			throw new AssertionError(privateType+" is not a private implementation of "+type);
	}
	
	@SuppressWarnings("all")
	private <R> Query<S,R> reveal(Query<T,R> query) {
		return (Query<S,R>) query;
	}
	
	
	public S reveal(T object) {
		return Utils.reveal(object,privateType);
	}
}
