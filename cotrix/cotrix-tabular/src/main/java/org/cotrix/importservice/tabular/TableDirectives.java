package org.cotrix.importservice.tabular;

import org.cotrix.importservice.Directives;

/**
 * An implementation of {@link Directives} for importing tabular data based on a mapping.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of parsed data
 * @param <M> the type of the mapping
 */
public class TableDirectives<T,M> implements Directives<T> {


	private final M mapping;
	
	/**
	 * Creates an instance with a given mapping.
	 * @param mapping the mapping
	 */
	public TableDirectives(M mapping) {
		this.mapping=mapping;
	}
	
	/**
	 * Returns the mapping for this directives.
	 * @return the mapping
	 */
	public M mapping() {
		return mapping;
	}
}
