package org.cotrix.io.parse;

import java.io.InputStream;


/**
 * Parses codelists according to given directives.
 * 
 * @author Fabio Simeoni
 *
 */
public interface ParseService {

	
	/**
	 * Parses a codelist according to given directives.
	 * @param stream the stream
	 * @param directives the directives
	 * @return the codelist
	 */
	<T> T parse(InputStream stream, ParseDirectives<T> directives);
	

}
