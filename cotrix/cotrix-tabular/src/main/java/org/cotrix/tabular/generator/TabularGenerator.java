package org.cotrix.tabular.generator;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.tabular.model.Tabular;

/**
 * This class is created only for test reasons.
 * 
 * It can be used to generate a test set of codes in a tabular form. Tabular
 * forms are usually reflected in CSV or Excell format.
 * 
 * The 'Tabular' represents a collection of codes. These codes can have
 * relations. The relations can be 1-1. 1-n and n-n.
 * 
 * 
 * @author Erik
 * 
 */
public class TabularGenerator {

	public static final String HEADER = "column";

	CodeGenerator g = new CodeGenerator();

	public Tabular generate(int rows, int columns) {
		List<List<String>> columnListSource = g.generateCodeColumns(rows,
				columns);
		Tabular tabular = new Tabular();
		List<List<String>> rowListDestination = new ArrayList<List<String>>();
		tabular.setRows(rowListDestination);

		addHeader2Tabular(tabular, columns);
		// mirror the columnlist on the diagonal axis
		for (int r = 0; r < rows; r++) {
			// build up a row
			List<String> row = new ArrayList<String>();
			// add row to the list
			rowListDestination.add(row);
			for (int c = 0; c < columns; c++) {
				// read the cell from the column oriented list
				String cell = columnListSource.get(c).get(r);
				row.add(cell);
			}
		}
		return tabular;
	}

	private void addHeader2Tabular(Tabular tabular, int columns) {
		List<String> headers = new ArrayList<String>();
		for (int c = 1; c <= columns; c++) {
			headers.add(HEADER + c);
		}
		tabular.setHeader(headers);
	}

}
