package org.cotrix.domain.validation;

import static java.lang.String.*;
import static java.util.Arrays.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.utils.ScriptEngine.*;

import java.util.List;

public enum Validators implements Validator {
	
	max_length($value+".length <= %s","length"),
	
	min_length($value+".length >= %s","length"),
	
	custom("%s","expression")
	
	;
	
	
	
	
	
	
	private final List<String> params;
	private final String template;
	
	Validators(String template,String ... params) {
		this.template=template;
		this.params= asList(params);
	}
	
	@Override
	public Constraint instance(Object... params) throws IllegalArgumentException {
		
		valid("validator's parameters",params);
		int size = this.params.size();
		
		if (params.length!=size) {
			String msg = format("wrong number of parameters for validator %s: expected %s, found %s (%s)",name(),size,params.length, asList(params));
			throw new IllegalArgumentException(msg);
		}
		
		return new Constraint(name(),format(template,(Object[]) params));
	}
	
	@Override
	public List<String> parameterNames() {
		return params;
	}

}

