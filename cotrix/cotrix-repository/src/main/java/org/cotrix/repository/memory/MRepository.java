package org.cotrix.repository.memory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.utils.Utils;
import org.cotrix.repository.Repository;
import org.cotrix.repository.query.Query;


public class MRepository<T, S extends Identified.Abstract<S>> implements Repository<T> {

	private final Class<T> type;
	private final Class<S> privateType;
	private final MStore store;
	
	public MRepository(MStore store, Class<T> type, Class<S> privateType) {
		this.store=store;
		this.type=type;	
		
		//validate types, as the compiler cannot
		validParameters(type,privateType);
		
		this.privateType=privateType;
	}
	
	@Override
	public void add(T object) {
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
	private void validParameters(Class<?> type, Class<?> privateType) {
		type.isAssignableFrom(privateType);
	}
		
	//helper
	private S reveal(T object) {
		return Utils.reveal(object,privateType);
	}
	
	//helper
	private T reveal(S object) {
		return Utils.reveal(object,type);
	}
	
	@SuppressWarnings("all")
	private <R> MQuery<T,R> revealQuery(Query<T,R> query) {
		return (MQuery<T,R>) Utils.reveal(query,MQuery.class);
	}
	
	@Override
	public <R> Iterator<R> queryFor(Query<T,R> query) {
		
		MQuery<T,R> cbQuery = revealQuery(query);
		
		List<R> results = new ArrayList<R>();
		
		int count = 1;
		for (S object: store.getAll(privateType))
			if (count<query.range().from())
				continue;
			else 
				if (count>query.range().to())
					break;
				else {
					T publicObject = reveal(object);
					R result = cbQuery.yieldFor(publicObject);
					if (result!=null) {
						results.add(result);
						count++;
					}
				}
				
		return results.iterator();
	}
}
