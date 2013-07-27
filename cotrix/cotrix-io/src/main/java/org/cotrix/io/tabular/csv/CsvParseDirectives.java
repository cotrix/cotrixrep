package org.cotrix.io.tabular.csv;

import org.cotrix.io.parse.ParseDirectives;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.tabular.Table;

/**
 * {@link ParseDirectives} for CSV streams
 * 
 * @author Fabio Simeoni
 *
 */
public class CsvParseDirectives implements ParseDirectives<Table> {

	//like for parsing, we reuse directives in the virtual repository.
	private final CsvCodelist options;
	
	
	/**
	 * Creates an instance with given parsing options.
	 * @param options the options.
	 */
	public CsvParseDirectives() {
		
		this.options=new CsvCodelist("unused", "unused",1);
	}
	
	/**
	 * Returns the parsing options of this directives.
	 * @return the options
	 */
	public CsvCodelist options() {
		return options;
	}
}
