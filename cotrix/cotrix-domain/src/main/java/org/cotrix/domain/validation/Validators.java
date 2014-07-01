package org.cotrix.domain.validation;

import static java.lang.String.*;
import static java.util.Arrays.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.utils.ScriptEngine.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Validators implements Validator {
	
	max_length($value+".length <= %s","length"),
	
	min_length($value+".length >= %s","length"),
	
	
	
	number(format("!isNaN(%s)",$value)),
	
	atleast(format("!isNaN(%1$s) && %1$s >= %2$s",$value,"%s"),"number"),

	greater(format("!isNaN(%1$s) && %1$s > %2$s",$value,"%s"),"number"),
	
	atmost(format("!isNaN(%1$s) && %1$s <= %2$s",$value,"%s"),"number"),
	
	smaller(format("!isNaN(%1$s) && %1$s < %2$s",$value,"%s"),"number"),
	
	between(format("!isNaN(%1$s) && %1$s >= %2$s && %1$s <= %2$s",$value,"%s"),"min","max"),
		
	
	date(format("!isNaN(new Date(%s).getTime())",$value)),
	
	before(format("!isNaN(new Date(%1$s).getTime()) && (new Date(%1$s).getTime() < new Date('%2$s').getTime())",$value,"%s"),"date"),

	after(format("!isNaN(new Date(%1$s).getTime()) && (new Date(%1$s).getTime() > new Date('%2$s').getTime())",$value,"%s"),"date"),
	
	between_dates(format("!isNaN(new Date(%1$s).getTime()) " +
			"&& (new Date(%1$s).getTime() > new Date('%2$s').getTime())" +
			"&& (new Date(%1$s).getTime() < new Date('%2$s').getTime())",$value,"%s"),"start","end"),
	
	regexp("/%s/.test("+$value+")","expression"),
	
	custom("%s","expression")
	
	;

	
	private final List<String> names;
	private final String template;
	private final static String paramErrorMsg ="wrong number of parameters for validator %s: expected %s, found %s (%s)";
	
	Validators(String template,String ... params) {
		this.template=template;
		this.names= asList(params);
	}
	
	@Override
	public Constraint instance(Object... params) throws IllegalArgumentException {
		
		valid("validator's parameters",params);
		int size = this.names.size();
		
		if (params.length!=size) {
			String msg = format(paramErrorMsg,name(),size,params.length, asList(params));
			throw new IllegalArgumentException(msg);
		}
		
		String expression = format(template,(Object[]) params);
				
		Map<String,String> paramMap = new HashMap<>();
		
		for (int i=0; i< names.size(); i++)
			paramMap.put(names.get(i), params[i].toString());
		
		return new Constraint(name(),expression,paramMap);
	}
	
	@Override
	public List<String> parameterNames() {
		return names;
	}
}

