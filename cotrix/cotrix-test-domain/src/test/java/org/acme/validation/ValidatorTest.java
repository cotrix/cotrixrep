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
	public void regexp() {
		
		Constraint v = regexp.instance("hel.o");
		
		boolean outcome = Boolean.valueOf(engine.eval(v.expression()).with("helo"));
		
		assertFalse(outcome);
		
		outcome = Boolean.valueOf(engine.eval(v.expression()).with("hello"));
		
		assertTrue(outcome);

		outcome = Boolean.valueOf(engine.eval(v.expression()).with("helllo"));
		
		assertFalse(outcome);

	}
	
	@Test
	public void isNumber() {
		
		Constraint v = number.instance();
		
		boolean outcome = Boolean.valueOf(engine.eval(v.expression()).with("hello"));
		
		assertFalse(outcome);
		
		outcome = Boolean.valueOf(engine.eval(v.expression()).with("12"));
		
		assertTrue(outcome);

		outcome = Boolean.valueOf(engine.eval(v.expression()).with("-14.56"));
		
		assertTrue(outcome);

	}
	
	@Test
	public void atLeast() {
		
		Constraint v = atleast.instance(10);
		
		boolean outcome = Boolean.valueOf(engine.eval(v.expression()).with("9"));
		
		assertFalse(outcome);
		
		outcome = Boolean.valueOf(engine.eval(v.expression()).with("10"));
		
		assertTrue(outcome);

		outcome = Boolean.valueOf(engine.eval(v.expression()).with("11.3"));
		
		assertTrue(outcome);

	}
	
	@Test
	public void greater() {
		
		Constraint v = greater.instance(10);
		
		boolean outcome = Boolean.valueOf(engine.eval(v.expression()).with("10"));
		
		assertFalse(outcome);
		
		outcome = Boolean.valueOf(engine.eval(v.expression()).with("11"));
	
		assertTrue(outcome);
	}
	
	@Test
	public void atMost() {
		
		Constraint v = atmost.instance(10);
		
		boolean outcome = Boolean.valueOf(engine.eval(v.expression()).with("11"));
		
		assertFalse(outcome);
		
		outcome = Boolean.valueOf(engine.eval(v.expression()).with("9"));
		
		assertTrue(outcome);

		outcome = Boolean.valueOf(engine.eval(v.expression()).with("7.5"));
		
		assertTrue(outcome);

	}
	
	
	@Test
	public void smaller() {
		
		Constraint v = smaller.instance(10);
		
		boolean outcome = Boolean.valueOf(engine.eval(v.expression()).with("10"));
		
		assertFalse(outcome);
		
		outcome = Boolean.valueOf(engine.eval(v.expression()).with("9"));
	
		assertTrue(outcome);
	}
	
	
	@Test
	public void between() {
		
		Constraint v = between.instance(10,15);
		
		boolean outcome = Boolean.valueOf(engine.eval(v.expression()).with("9"));
		
		assertFalse(outcome);
		
		outcome = Boolean.valueOf(engine.eval(v.expression()).with("11"));
		
		assertTrue(outcome);
		
		outcome = Boolean.valueOf(engine.eval(v.expression()).with("15"));
		
		assertTrue(outcome);
		
		outcome = Boolean.valueOf(engine.eval(v.expression()).with("16"));
	
		assertFalse(outcome);
	}
	
	@Test
	public void custom() {
		
		Constraint v = custom.instance("true");
		
		boolean outcome = Boolean.valueOf(engine.eval(v.expression()).withNothing());
		
		assertTrue(outcome);
	}
}
