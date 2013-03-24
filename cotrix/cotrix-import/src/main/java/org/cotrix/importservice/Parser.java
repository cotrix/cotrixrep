package org.cotrix.importservice;

import java.io.InputStream;

/**
 * Parses data according to {@link Directives}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the parsed data
 * @param <D> the type of parsing directives
 */
public interface Parser<T, D extends Directives<T>> {

	/**
	 * Parses data according to {@link Directives}.
	 * @param stream the data to parse
	 * @param directives the directives
	 * @return the parsed data
	 */
	T parse(InputStream stream, D directives);
	
	/**
	 * Returns the type of {@link Directives} used by this parser.
	 * @return the type of {@link Directives}
	 */
	Class<D> directedBy();
	
}
