package org.cotrix.domain.values;

import org.cotrix.domain.validation.Constraints;



public interface ValueType {
	
	String defaultValue();
	
	boolean isValid(String value);
	
	Constraints constraints();
	
}
