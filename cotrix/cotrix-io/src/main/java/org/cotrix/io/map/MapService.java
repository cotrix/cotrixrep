package org.cotrix.io.map;


/**
 * Maps codelists into domain objects according to given directives.
 * 
 * @author Fabio Simeoni
 *
 */
public interface MapService {


	/**
	 * Maps a codelist into a domain object.
	 * 
	 * @param codelist the codelist
	 * @param directives the directives
	 * @return the domain object
	 */
	<T> Outcome map(T codelist, MapDirectives<T> directives);

}
