package org.cotrix.io.tabular.csv.serialise;

import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.tabular.Table;

public class Table2CsvDirectives implements SerialisationDirectives<Table> {

	//like for parsing, we reuse directives in the virtual repository.
	private final CsvCodelist options;
	
	
	/**
	 * Creates an instance with given parsing options.
	 * @param options the options.
	 */
	public Table2CsvDirectives() {
		
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
