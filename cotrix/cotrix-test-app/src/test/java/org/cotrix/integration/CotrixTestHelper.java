/**
 * 
 */
package org.cotrix.integration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixTestHelper {

	private WebDriver driver;

	/**
	 * @param driver
	 */
	public CotrixTestHelper(WebDriver driver) {
		this.driver = driver;
	}

	public void login(String username, String password) {
		driver.findElement(By.id("gwt-debug-user.login")).click();
		driver.findElement(By.id("gwt-debug-user.login.username")).clear();
		driver.findElement(By.id("gwt-debug-user.login.username")).sendKeys(username);
		driver.findElement(By.id("gwt-debug-user.login.password")).clear();
		driver.findElement(By.id("gwt-debug-user.login.password")).sendKeys(password);
		driver.findElement(By.id("gwt-debug-user.login.submit")).click();

		(new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElementLocated(By.id("gwt-debug-user.username"), username));
	}

	public void signup(String username, String password, String email) {
		driver.findElement(By.id("gwt-debug-user.signup")).click();
		driver.findElement(By.id("gwt-debug-signup.username")).clear();
		driver.findElement(By.id("gwt-debug-signup.username")).sendKeys(username);
		driver.findElement(By.id("gwt-debug-signup.password")).clear();
		driver.findElement(By.id("gwt-debug-signup.password")).sendKeys(password);
		driver.findElement(By.id("gwt-debug-signup.email")).clear();
		driver.findElement(By.id("gwt-debug-signup.email")).sendKeys(email);
		driver.findElement(By.id("gwt-debug-signup.create")).click();

		(new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElementLocated(By.id("gwt-debug-user.username"), username));
	}

	public void resetSession() {
		/*
		page reload fails, can't be used
		driver.manage().deleteAllCookies();
		driver.navigate().refresh();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);*/
	}

}
