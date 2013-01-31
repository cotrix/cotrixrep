package org.cotrix.domain.coderelation;

import org.cotrix.domain.conceptrelation.Concept;

/**
 * ConvenienceClass in order to store the column number as the expression of the Concept.
 * 
 * Column numnbers in a tabular format do start at column 0.
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class ConceptTabular extends Concept {
	private int columnNumber;

	public ConceptTabular() {
		super();
	}

	public ConceptTabular(int columnNumber) {
		super();
		useForValue(columnNumber);
		this.columnNumber = columnNumber;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		useForValue(columnNumber);
		this.columnNumber = columnNumber;
	}

	private void useForValue(int columnNumber) {
		if (this.getValue() == null) {
			this.setValue(new Integer(columnNumber).toString());
		}
	}

}
