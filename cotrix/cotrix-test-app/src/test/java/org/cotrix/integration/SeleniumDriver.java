/**
 * 
 */
package org.cotrix.integration;

import org.openqa.selenium.WebDriver;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public enum SeleniumDriver {
	HTMLUNIT("org.cotrix.integration.AjaxHtmlUnitDriver"),
	CHROME("org.openqa.selenium.chrome.ChromeDriver"),
	FIREFOX("org.openqa.selenium.firefox.FirefoxDriver"),
	SAFARI("org.openqa.selenium.safari.SafariDriver")
	;

	private String driverName;

	/**
	 * @param driverName
	 */
	private SeleniumDriver(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverName() {
		return driverName;
	}

	public WebDriver instantiate() {
		try {
			return (WebDriver) Class.forName(driverName).newInstance();
		} catch(final InstantiationException e){
			throw new IllegalStateException("Failed instantiation of "+driverName, e);
		} catch(final IllegalAccessException e){
			throw new IllegalStateException("Failed instantiation of "+driverName, e);
		} catch(final ClassNotFoundException e){
			throw new IllegalStateException("Failed instantiation of "+driverName, e);
		}
	}

}
