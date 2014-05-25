package org.cotrix.domain.validation;

import java.util.List;

//yields constraints upon provision of parameters
public interface Validator {

	String name();
	
	List<String> parameterNames();
	
	Constraint instance(Object ... params) throws IllegalArgumentException;
	
}
