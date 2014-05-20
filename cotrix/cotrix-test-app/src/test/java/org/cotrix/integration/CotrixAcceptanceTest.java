/**
 * 
 */
package org.cotrix.integration;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class CotrixAcceptanceTest {
	
	protected static WebDriver driver;
	protected static CotrixTestHelper helper;
	
	@BeforeClass
	public static void setUp() throws Exception {
		TestConfiguration testConfiguration = new TestConfiguration();
		driver = testConfiguration.getDriver();
		driver.get(testConfiguration.getBaseUrl());
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		helper = new CotrixTestHelper(driver);
	}
	
	@AfterClass
	public static void shutdown() {
		driver.close();
	}

}
