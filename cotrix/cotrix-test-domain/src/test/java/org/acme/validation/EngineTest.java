package org.acme.validation;

import static org.cotrix.domain.utils.ScriptEngine.*;

import javax.inject.Inject;

import org.cotrix.domain.utils.ScriptEngine;
import org.cotrix.domain.utils.ScriptEngineProvider;
import org.cotrix.test.ApplicationTest;
import org.junit.Assert;
import org.junit.Test;

public class EngineTest extends ApplicationTest {

	@Inject
	ScriptEngine engine;
	
	@Test
	public void factory() {
		
		ScriptEngineProvider.engine().eval(null,"'hello workd'");
		
			
	}
	
	@Test
	public void eval() {
		
		String output = engine.eval("hello", $value+"+ ' world'");
		
		Assert.assertEquals("hello world",output);
			
	}
}
