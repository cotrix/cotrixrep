package org.cotrix.repository.impl.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.cotrix.common.Utils;
import org.cotrix.domain.trait.Identified;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Range;
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

	public abstract Collection<? extends R> executeOn(MemoryRepository<? extends T> repository);
	
	
	/**
	 * Returns one or more results from a given object.
	 * @param object the object
	 * @return the results, or <code>null</code> if the object does not match the query.
	 */
	public Collection<R> execute(MemoryRepository<? extends T> repository) {
		
		
		List<R> results = new ArrayList<R>(executeOn(repository));
		
		List<R> excludes = new ArrayList<R>();
		
		for (R result : results)
			if (result instanceof Identified && 
					excludes().contains(Identified.class.cast(result).id()))
				excludes.add(result);
		
		results.removeAll(excludes);
		
		int count = 1;
	
		List<R> range = new ArrayList<R>();
		
		if (range()==Range.ALL)
			range.addAll(results);
		
		else
			
			nextResult: for (R result: results) {
		
				//include only in range
				if (count<range().from()) {
					count++;
					continue nextResult;
				}
				else {
					if (count>range().to())
						break;
					else { 
						range.add(result);
						count++;
					}
				}
			}
			
				
		if (criterion()!=null)
			Collections.sort(range,reveal(criterion()));
		 
		return range;
		
	}
	
	
	//helper
	@SuppressWarnings("all")
	private MCriterion<R> reveal(Criterion<R> criterion) {
		return Utils.reveal(criterion, MCriterion.class);
	}
}
