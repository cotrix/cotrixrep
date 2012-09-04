package org.cotrix.tabular.generator;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.core.CotrixException;

public class CodeGenerator {

	public static final String CHARACTER = "x";

	private UniquenessRule u = new UniquenessRule();

	/**
	 * Generate columns with a good spread of values
	 * 
	 * 
	 * 
	 * @param rows
	 * @param columns
	 * @return
	 */
	public List<List<String>> generateCodeColumns(int rows, int columns) {
		List<List<String>> columnList = new ArrayList<List<String>>();
		Double[] uniquenessOrder = u.calculateUniquenessRandom(columns);
		for (int c = 0; c < columns; c++) {
			// calculate the possible values for a column, respecting the
			// uniqueness level
			List<String> values = calculateColumnValues(rows, c,
					uniquenessOrder[c]);
			// duplicate values to fill up, if necessary
			spreadValues(values, rows);
			// add this list as such
			columnList.add(values);
		}
		return columnList;
	}

	/**
	 * Duplicate values if there need to be more.
	 * 
	 * 
	 * @param values
	 * @param rows
	 */
	protected void spreadValues(List<String> values, int rows) {
		if (values.size() == 0) {
			throw new CotrixException(
					"Size cannot be 0, at least one value needs to be provided");
		}

		int size = values.size();
		int toAdd = rows - size;
		int toAddPerElement = toAdd / size;
		// loop through the desired elements
		for (int i = 0; i < rows; i++) {
			String found;
			int lastIndex = values.size() - 1;
			if (i > lastIndex) {
				// in case of rest elements because of the spreading in case of
				// odd number of elements.
				found = values.get(lastIndex);
				values.add(found);
			} else {
				found = values.get(i);
				for (int a = 0; a < toAddPerElement; a++) {
					// duplicate the elements
					values.add(i, found);
				}
				i = i + toAddPerElement;
			}
		}
	}

	/**
	 * Generate the distinct list of values
	 * 
	 * 
	 * @param rows
	 *            (number of elements needed)
	 * @param column
	 * @param uniqueness
	 *            value between 0 and 1
	 * @return
	 */
	protected List<String> calculateColumnValues(int rows, int column,
			Double uniqueness) {
		List<String> values = new ArrayList<String>();

		// calculate the number of unique elements in a particular column
		double d = rows * uniqueness;
		int nrOfUniqueElements = (int) Math.rint(d);

		// when there is no uniqueness level, there should be at least 1
		// element.
		if (uniqueness == 0 || nrOfUniqueElements == 0) {
			nrOfUniqueElements = 1;
		}

		// do not use the index but the an appropriate value
		column++;

		// build a collection of columns values
		for (int i = 1; i <= nrOfUniqueElements; i++) {
			// System.out.println(f);
			// System.out.println(f + i);
			String value = column + CHARACTER + i;
			values.add(value);
		}
		return values;
	}
}
