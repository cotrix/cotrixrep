package org.cotrix.domain.conceptrelation;

/**
 * A column in a tabular format is the expression of values of a certain
 * concept.
 * 
 * As the conceptvalue, the header value of a column could be used (if present).
 * 
 * @author Erik van Ingen
 * 
 */
public class Concept {

	private String value;

	public Concept() {
		super();
	}

	public Concept(String conceptName) {
		super();
		this.value = conceptName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
