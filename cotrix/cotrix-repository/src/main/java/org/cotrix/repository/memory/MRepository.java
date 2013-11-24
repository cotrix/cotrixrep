package org.cotrix.repository.memory;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.common.Utils;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.trait.Identified;
import org.cotrix.repository.Repository;
import org.cotrix.repository.query.Query;


public class MRepository<T, S extends Identified.Abstract<S>> implements Repository<T> {

	private final Class<T> type;
	private final Class<S> privateType;
	private final MStore store;
	private final IdGenerator generator;
	
	public MRepository(MStore store, Class<T> type, Class<S> privateType, IdGenerator generator) {
		
		this.store=store;
		this.type=type;	
		this.generator=generator;
		
		//validate types, as the compiler cannot
		validateParameters(type,privateType);
		
		this.privateType=privateType;
	}
	
	protected final String generateId() {
		return generator.id();
	}
	
	@Override
	public void add(T object) {
		
		S priv = reveal(object);
		
		if (priv.id()==null)
			priv.setId(generator.id());
		
		store.add(reveal(object),privateType);
	}
	
	@Override
	public T lookup(String id) {
		return type.cast(store.lookup(id,privateType));
	}
	
	@Override
	public void remove(String id) {
		store.remove(id,privateType);
	}
	
	public void update(T changeset) {
		
		store.update(reveal(changeset),privateType);
	};
	
	
	//valid parameters
	private void validateParameters(Class<?> type, Class<?> privateType) {
		if (!type.isAssignableFrom(privateType))
			throw new AssertionError(privateType+" is not a private implementation of "+type);
	}
		
	//helper
	private S reveal(T object) {
		return Utils.reveal(object,privateType);
	}
	
	public List<T> getAll() {
		List<T> all = new ArrayList<T>();
		for (S s : store.getAll(privateType))
			all.add(reveal(s));
		return all;
	}
	
	//helper
	public T reveal(S object) {
		return Utils.reveal(object,type);
	}
	
	@SuppressWarnings("all")
	private <R> MQuery<T,R> revealQuery(Query<T,R> query) {
		return (MQuery<T,R>) Utils.reveal(query,MQuery.class);
	}
	
	@Override
	public <R> Iterable<R> queryFor(Query<T,R> query) {
		
		MQuery<T,R> cbQuery = revealQuery(query);
	
		return cbQuery.execute(this);
	}
	
	@Override
	public int size() {
		return store.size(privateType);
	}
}
