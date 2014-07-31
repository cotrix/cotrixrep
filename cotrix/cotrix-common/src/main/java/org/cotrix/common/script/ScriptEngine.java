package org.cotrix.common.script;

public interface ScriptEngine {

	public static String $value = "$value"; 
	
	WithClause eval(String expression);
	
	public static interface WithClause {
		
		String with(String value);
		
		String withNothing();
	}
}