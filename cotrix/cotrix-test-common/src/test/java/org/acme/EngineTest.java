package org.acme;

import static org.cotrix.common.script.ScriptEngine.*;

import javax.inject.Inject;

import org.cotrix.common.script.ScriptEngine;
import org.cotrix.common.script.ScriptEngineProvider;
import org.cotrix.test.ApplicationTest;
import org.junit.Assert;
import org.junit.Test;

public class EngineTest extends ApplicationTest {

	@Inject
	ScriptEngine engine;
	
	@Test
	public void factory() {
		
		ScriptEngineProvider.engine().eval("'hello workd'").withNothing();
		
			
	}
	
	@Test
	public void eval() {
		
		String output = engine.eval($value+"+ ' world'").with("hello");
		
		Assert.assertEquals("hello world",output);
			
	}
}
