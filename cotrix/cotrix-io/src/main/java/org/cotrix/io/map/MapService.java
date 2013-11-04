package org.cotrix.io.map;

import org.cotrix.common.Outcome;
import org.cotrix.domain.Codelist;


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
	 * @return the outcome the task.
	 */
	<T> Outcome<Codelist> map(T codelist, MapDirectives<T> directives);

}
