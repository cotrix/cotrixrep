package org.cotrix.domain.attributes;

import java.util.List;

import org.cotrix.domain.validation.Constraint;



public interface ValueType {
	
	boolean isRequired();
	
	boolean isValid(String value);
	
	List<Constraint> constraints();
	
	String constraint();
}
