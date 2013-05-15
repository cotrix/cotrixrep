package org.cotrix.io.tabular.csv;

import org.cotrix.io.tabular.TableImportDirectives;
import org.virtualrepository.csv.CsvCodelist;

/**
 * {@link TableImportDirectives} for codelists available in CSV format.

 * 
 * @author Fabio Simeoni
 *
 */
public class CsvImportDirectives extends TableImportDirectives {

	private final CsvCodelist options;

	/**
	 * Creates an instance with the name of the column that contains codes.
	 * 
	 * @param column the code column name
	 */
	public CsvImportDirectives(String column) {
		super(column);
		this.options = new CsvCodelist("unknown",column,0);
	}

	/**
	 * Returns the format options of these directives.
	 * @return the options
	 */
	public CsvCodelist format() {
		return options;
	}

}
