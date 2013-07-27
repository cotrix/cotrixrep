package org.cotrix.io.parse;

import java.io.InputStream;

import org.cotrix.io.Task;

/**
 * A {@link Task} that parses codelists.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of parsed codelists
 * @param <D> the type of task directives
 */
public interface ParseTask<T,D extends ParseDirectives<T>> extends Task<D>  {

	/**
	 * Parses a codelist using given directives.
	 * @param codelist the codelist
	 * @param directives the directives
	 * @return the parsed codelist
	 * @throws Exception if the codelist cannot be parsed
	 */
	T parse(InputStream codelist, D directives) throws Exception;
	
}
