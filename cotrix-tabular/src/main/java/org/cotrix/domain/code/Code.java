package org.cotrix.domain.code;

import org.cotrix.domain.documentation.Documentation;

/**
 * A code is the epression of on element of a dimension, attribute or concept.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class Code extends Documentation {

	protected String value;

	/**
	 * Default constructor
	 */
	public Code() {
	}

	/**
	 * Constructor for providing directly the code
	 * 
	 * 
	 * @param value
	 */
	public Code(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Code other = (Code) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
