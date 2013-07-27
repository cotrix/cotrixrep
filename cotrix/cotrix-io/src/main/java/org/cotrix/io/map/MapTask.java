package org.cotrix.io.map;

import org.cotrix.domain.Codelist;
import org.cotrix.io.Task;

/**
 * A {@link Task} that maps codelists onto domain objects.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of codelists
 * @param <D> the type of task directives
 */
public interface MapTask<T,D extends MapDirectives<T>> extends Task<D>  {

	/**
	 * Maps a codelist onto a domain object.
	 * @param list the codelist
	 * @param directives the directives
	 * @return the domain object
	 * @throws Exception if the list cannot be mapped
	 */
	Codelist map(T list, D directives) throws Exception;
	
}
