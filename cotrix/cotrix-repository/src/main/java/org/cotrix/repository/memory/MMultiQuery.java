package org.cotrix.repository.memory;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cotrix.repository.Filter;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.impl.BaseMultiQuery;

/**
 * Partial implementation of a {@link MultiQuery} evaluated against preloaded objects
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of objects
 * @param <R> the type of query results
 * 
 */
public abstract class MMultiQuery<T,R> extends BaseMultiQuery<T,R> implements MQuery<T,Collection<R>> {

	/**
	 * Returns one or more results from a given object.
	 * @param object the object
	 * @return the results, or <code>null</code> if the object does not match the query.
	 */
	public abstract Collection<? extends R> executeOn(MemoryRepository<? extends T> repository);
	
	
	/**
	 * Returns one or more results from a given object.
	 * @param object the object
	 * @return the results, or <code>null</code> if the object does not match the query.
	 */
	@SuppressWarnings("unchecked")
	public Collection<R> execute(MemoryRepository<? extends T> repository) {
		
		Collection<? extends R> results = executeOn(repository);
		
		List<R> filtered = new ArrayList<R>();
		
		int count = 1;
		
		nextResult: for (R result: results) {
		
			for (Filter<T> f : filters())
				//include only matches
				if (!reveal(f,MFilter.class).matches(result))
					continue nextResult;
			
			//include only in range
			if (count<range().from()) {
				count++;
				continue nextResult;
			}
			else {
				if (count>range().to())
					break;
				else { 
					filtered.add(result);
					count++;
				}
			}
			
		}
		
		return filtered;
		
	}
	
}
