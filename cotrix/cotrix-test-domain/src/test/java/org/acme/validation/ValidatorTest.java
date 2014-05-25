package org.acme.validation;

import static org.cotrix.domain.validation.Validators.*;
import static org.junit.Assert.*;

import org.cotrix.domain.utils.JavascriptEngine;
import org.cotrix.domain.utils.ScriptEngine;
import org.cotrix.domain.validation.Constraint;
import org.junit.BeforeClass;
import org.junit.Test;

public class ValidatorTest {

	ScriptEngine engine = new JavascriptEngine();
	
	@BeforeClass
	public static void setup() {
		System.setProperty("org.slf4j.simpleLogger.log.org.cotrix","trace");
	}
	
	@Test
	public void maxlength() {
		
		Constraint v = max_length.instance("2");
		
		boolean outcome = Boolean.valueOf(engine.eval(v.expression()).with("123"));
		
		assertFalse(outcome);
		
		outcome = Boolean.valueOf(engine.eval(v.expression()).with("12"));
		
		assertTrue(outcome);
		
	}
	
	@Test
	public void minlength() {
		
		Constraint v = min_length.instance("2");
		
		boolean outcome = Boolean.valueOf(engine.eval(v.expression()).with("1"));
		
		assertFalse(outcome);
		
		outcome = Boolean.valueOf(engine.eval(v.expression()).with("12"));
		
		assertTrue(outcome);
		
	}
	
	@Test
	public void custom() {
		
		Constraint v = custom.instance("true");
		
		boolean outcome = Boolean.valueOf(engine.eval(v.expression()).withNothing());
		
		assertTrue(outcome);
	}
}
