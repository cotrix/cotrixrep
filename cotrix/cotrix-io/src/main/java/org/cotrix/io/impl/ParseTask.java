package org.cotrix.io.impl;

import java.io.InputStream;


/**
 * A parsing task.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of parsed objects
 * @param <D> the type of task directives
 */
public interface ParseTask<T,D> extends Task<D>  {

	/**
	 * Parses a stream using given directives.
	 * @param stream the stream
	 * @param directives the directives
	 * @return the result of parsing
	 * 
	 * @throws Exception if the stream cannot be parsed
	 */
	T parse(InputStream stream, D directives) throws Exception;
	
}
