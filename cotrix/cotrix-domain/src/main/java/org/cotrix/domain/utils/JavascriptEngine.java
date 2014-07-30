package org.cotrix.domain.utils;

import static java.lang.String.*;

import javax.inject.Singleton;
import javax.script.ScriptEngineManager;

@Singleton
public class JavascriptEngine implements ScriptEngine {

	//private static Logger log = LoggerFactory.getLogger(JavascriptEngine.class);
	
	private static javax.script.ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
	
	@Override
	public WithClause eval(final String expression) {
		
		 return new WithClause() {
			
			@Override
			public String with(String value) {
				try {
					
					if (value==null || value.isEmpty())
						value="null";
					
					//log.trace("evaluating \"{}\" with {}", expression, (value=="null" || value.isEmpty())?"<no value>":": "+value);
					
					engine.put($value, value);
					
					return valueOf(engine.eval(expression));
				}
				catch(Exception e) {
					throw new RuntimeException("cannot evaluate script \n"+expression+"\n over "+value+" (see cause)",e);
				}
			}
			
			@Override
			public String withNothing() {
				return with("<no-value>");
			}
		};

	}
}
