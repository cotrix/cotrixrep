package org.cotrix.io;

import org.cotrix.common.Outcome;


/**
 * Maps objects according to directives.
 * 
 * @author Fabio Simeoni
 *
 */
public interface MapService {

	/**
	 * Directives for mapping.
	 * 
	 * @author Fabio Simeoni
	 *
	 * @param <T> the type of mapped object
	 */
	public interface MapDirectives<T> {

	}

	/**
	 * Maps a source object into a target object.
	 * 
	 * @param source the source
	 * @param directives the directives
	 * @return the outcome the task
	 * 
	 * @param <T> the source object type
	 * @param <S> the mapped object type
	 */
	<T,S> Outcome<S> map(T source, MapDirectives<S> directives);

}
