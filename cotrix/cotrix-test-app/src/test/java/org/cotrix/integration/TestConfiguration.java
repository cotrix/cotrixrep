/**
 * 
 */
package org.cotrix.integration;

import org.openqa.selenium.WebDriver;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TestConfiguration {

	private static final String SELENIUM_DRIVER_PROPERTY_NAME = "selenium.driver";
	private static final String URL_PROPERTY_NAME = "cotrix.url";
	private WebDriver driver;
	private String baseUrl;

	public TestConfiguration() {
		SeleniumDriver seleniumDriver = getSeleniumDriver();
		System.out.println("seleniumDriver: "+seleniumDriver);
		driver = seleniumDriver.instantiate();
		baseUrl = getUrl();
		System.out.println("baseUrl: "+baseUrl);
	}

	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @return the driver
	 */
	public WebDriver getDriver() {
		return driver;
	}

	private SeleniumDriver getSeleniumDriver() {
		String driverName = System.getProperty(SELENIUM_DRIVER_PROPERTY_NAME);
		
		if (driverName == null) return SeleniumDriver.HTMLUNIT;
		try {
			return SeleniumDriver.valueOf(driverName.toUpperCase());
		} catch(IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid selenium driver "+driverName, e);
		}
	}

	private String getUrl() {
		String url = getProperty(URL_PROPERTY_NAME);
		return url;
	}
	
	private String getProperty(String propertyName) {
		String propertyValue = System.getProperty(propertyName);
		if (propertyValue == null) throw new IllegalArgumentException("Missing system property "+propertyName);
		return propertyValue;
	}
}
