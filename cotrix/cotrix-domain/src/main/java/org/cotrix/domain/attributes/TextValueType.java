package org.cotrix.domain.attributes;

import static org.cotrix.common.Utils.*;

public class TextValueType implements AttributeValueType {

	public static int UNLIMITED = -1;
	
	private int length = UNLIMITED;
	
	
	public TextValueType length(int length) {
		
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
}
