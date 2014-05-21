package org.cotrix.domain.attributes;

import static org.cotrix.common.Utils.*;

public class Text implements ValueType {

	public static int UNLIMITED = -1;
	
	private int length = UNLIMITED;
	
	
	public Text length(int length) {
		
		positive("lenght", length);
		
		this.length=length;

		return this;
	}
	
	public int length() {
		return length;
	}
	
	@Override
	public boolean isValid(String value) {
		
		boolean valid = true;
		
		if (length!=UNLIMITED)
			valid= (length>=value.length());
		
		
		return valid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + length;
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
		Text other = (Text) obj;
		if (length != other.length)
			return false;
		return true;
	}
	
	
}
