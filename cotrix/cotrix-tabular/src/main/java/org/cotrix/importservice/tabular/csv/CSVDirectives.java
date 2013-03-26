package org.cotrix.importservice.tabular.csv;

import org.cotrix.importservice.tabular.TableDirectives;

/**
 * Partial implementation of {@link TableDirectives} specific to CSV data.
 * <p>
 * Include parsing {@link CSVOptions} in addition to the mapping.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of parsed data
 * @param <M> the type of the mapping
 */
public abstract class CSVDirectives<T,M> extends TableDirectives<T,M> {

	private final CSVOptions options;

	/**
	 * Creates an instance with a given mapping and given options.
	 * @param mapping the mapping
	 * @param options the options
	 */
	public CSVDirectives(M mapping, CSVOptions options) {
		super(mapping);
		this.options = options;
	}

	/**
	 * Returns the parsing options of these directives.
	 * @return the options
	 */
	public CSVOptions options() {
		return options;
	}

}
