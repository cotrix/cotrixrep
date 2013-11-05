package org.cotrix.io.tabular.csv.parse;

import org.cotrix.io.ParseService.ParseDirectives;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.tabular.Table;

/**
 * Directives to parse CSV streams
 * 
 * @author Fabio Simeoni
 *
 */
public class Csv2TableDirectives implements ParseDirectives<Table> {

	//like for parsing, we reuse directives in the virtual repository.
	private final CsvCodelist options;
	
	
	/**
	 * Creates an instance with given parsing options.
	 * @param options the options.
	 */
	public Csv2TableDirectives() {
		
		this.options=new CsvCodelist("unused", "unused",1);
	}
	
	/**
	 * Returns the parsing options of this directives.
	 * @return the options
	 */
	public CsvCodelist options() {
		return options;
	}

	@Override
	public String toString() {
		return "[header="+options.hasHeader()+", delimiter="+options.delimiter()+", encoding="+options.encoding()+", codeColumn="+options.codeColumn()+", quote character="+options.quote()+"]";
	}
	
	
}
