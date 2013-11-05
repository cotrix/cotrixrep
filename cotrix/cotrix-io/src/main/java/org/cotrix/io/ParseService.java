package org.cotrix.io;

import java.io.InputStream;

/**
 * Parses streams according to directives.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface ParseService {

	/**
	 * Parse directives.
	 * 
	 * @param <T> the type of parsed objects
	 */
	// implementation will look up dynamically, but interface provides safety and carries a type for output
	public interface ParseDirectives<T> {
	}

	/**
	 * Parses a stream according to given directives.
	 * 
	 * @param stream the stream
	 * @param directives the directives
	 * @return the parsing output
	 * 
	 * @param <T> the type of parsed objects
	 */
	<T> T parse(InputStream stream, ParseDirectives<T> directives);

}
