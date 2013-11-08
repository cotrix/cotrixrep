package org.cotrix.io.impl;

import java.io.OutputStream;

/**
 * A serialising task.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of serialised objects
 * @param <D> the type of task directives
 */
public interface SerialisationTask<T, D> extends Task<D> {

	/**
	 * Serialises a given object to a stream according to given directives.
	 * 
	 * @param object the object
	 * @param stream the stream
	 * @param directives the directives
	 * 
	 * @throws Exception if the given object cannot be serialised to the given stream according to the given directives.
	 */
	void serialise(T object, OutputStream stream, D directives) throws Exception;

}
