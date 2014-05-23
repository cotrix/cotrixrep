package org.cotrix.domain.attributes;

import static org.cotrix.common.Utils.*;

public class Text implements ValueType {
	
	public static final Text freetext = new Text();

	public static int UNLIMITED = -1;
	
	private int max = UNLIMITED;
	
	
	public Text max(int max) {
		
		positive("lenght", max);
		
		this.max=max;

		return this;
	}
	
	public int max() {
		return max;
	}
	
	@Override
	public boolean isValid(String value) {
		
		boolean valid = true;
		
		if (max!=UNLIMITED)
			valid= (max>=value.length());
		
		
		return valid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + max;
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
		if (max != other.max)
			return false;
		return true;
	}
	
	
}
