package org.cotrix.domain.utils;

public interface ScriptEngine {

	public static String $value = "$value"; 
	
	String eval(String value, String expression);
}