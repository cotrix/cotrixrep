package org.cotrix.domain.utils;

import javax.inject.Singleton;
import javax.script.ScriptEngineManager;

@Singleton
public class JavascriptEngine implements ScriptEngine {

	private static javax.script.ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
	
	@Override
	public String eval(String value, String expression) {
			try {
				
				engine.put($value, value);
				
				return (String) engine.eval(expression);
			}
			catch(Exception e) {
				throw new RuntimeException("cannot evaluate script \n"+expression+"\n over "+value+" (see cause)",e);
			}
		}
	}
