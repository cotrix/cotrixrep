package org.cotrix.importservice.tabular.model;

import java.util.Iterator;
import java.util.List;

/**
 * A model of tabular data for import.
 * @author Fabio Simeoni
 *
 */
public interface Table extends Iterable<Row> {

	/**
	 * Returns an iterator over the rows of the table.
	 * @return the iterator
	 */
	Iterator<Row> iterator();
	
	/**
	 * Returns the names of the columns of this table
	 * @return the columns names
	 */
	List<String> columns();
}
