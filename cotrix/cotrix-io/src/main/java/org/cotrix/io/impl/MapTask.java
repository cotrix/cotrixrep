package org.cotrix.io.impl;


/**
 * A mapping task.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the source object type
 * @param <S> the target object type
 * @param <D> the directive type
 */
public interface MapTask<T,S,D> extends Task<D>  {

	/**
	 * Maps a source object to a target object.
	 * @param source the source
	 * @param directives the directives
	 * @return the target
	 * 
	 * @throws Exception if the list cannot be mapped
	 */
	S map(T source, D directives) throws Exception;
	
}
