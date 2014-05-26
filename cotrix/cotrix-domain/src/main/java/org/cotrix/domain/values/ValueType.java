package org.cotrix.domain.values;

import java.util.List;

import org.cotrix.domain.validation.Constraint;



public interface ValueType {
	
	boolean isRequired();
	
	String defaultValue();
	
	boolean isValid(String value);
	
	List<Constraint> constraints();
	
	String constraint();
}
