package org.cotrix.integration;

import static org.junit.Assert.*;

import org.junit.*;
import org.openqa.selenium.By;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AuthTest extends CotrixAcceptanceTest {
	
	@Test
	public void testLogin() throws Exception {
		
		helper.resetSession();
		
		helper.login("federico", "federico");
		
		assertEquals("federico", driver.findElement(By.id("gwt-debug-user.username")).getText());
	}
	
	
	/* Conflict with previous test because resetSession method is not working
	@Test
	public void testSignup() throws Exception {
		
		helper.resetSession();
		
		helper.signup("mario", "mario", "mario@mario.it");
		
		assertEquals("mario", driver.findElement(By.id("gwt-debug-user.username")).getText());
	}*/
}
