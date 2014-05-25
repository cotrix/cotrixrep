package org.cotrix.domain.utils;

public interface ScriptEngine {

	public static String $value = "$value"; 
	
	WithClause eval(String expression);
	
	public static interface WithClause {
		
		String with(String value);
		
		String withNothing();
	}
}