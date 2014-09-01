package org.cotrix.neo.repository;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.cotrix.repository.Range;

public class PostProcessingIterator<R> implements Iterator<R> {
	
	public static interface PostProcessor<R> {
		
		boolean match(R result);
	}

	private final Iterator<R> inner;
	private final PostProcessor<R> processor;
	private final Range range; 
	
	private int count=1;
	
	private R next = null;
	
	public PostProcessingIterator(Iterator<R> inner, Range range, PostProcessor<R> processor) {
		this.inner=inner;
		this.range = range;
		this.processor=processor;
	}
	
	
	private boolean inRange() {
		return range==Range.ALL? true: count>range.from();
			
	}
	
	private boolean pastRange() {
		return range==Range.ALL? false: count>range.to();
			
	}
	
	@Override
	public boolean hasNext() {
		
		//still to be consumed
		if (next!=null)
			return true;
		
		//safer than recursion here
		while (true) {
		
			//no more required or potentially available
			if (pastRange() || !inner.hasNext())
				return false;
			
			R result = inner.next();
				
			if (processor.match(result)) {
				
				count++;
	
				//it's in range
				if (inRange()) {
					next = result;
					return true;
				}
			}
		}
		
	}

	@Override
	public R next() {
		
		if (next==null && !hasNext())
			throw new NoSuchElementException();
		
		R returned = next;
		
		next=null;
		
		return returned;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
		
	}	

	
}
