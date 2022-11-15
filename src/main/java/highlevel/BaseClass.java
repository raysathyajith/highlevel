package highlevel;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseClass {

	private static WebDriver driver;
	WebDriverWait wait;
	WebDriverWait pageLoaderWait;
	final int MAX_ELEMENT_LOADER_TIME = 20;
	final int MAX_PAGE_LOADER_TIME = 40;
	
	BaseClass(WebDriver wdriver) {
		driver = wdriver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(MAX_ELEMENT_LOADER_TIME));
		pageLoaderWait = new WebDriverWait(driver, Duration.ofSeconds(MAX_PAGE_LOADER_TIME));
	}
	
	public void waitForLoader() {
		try {
			By loader = By.xpath("//*[@class='loading']");
			pageLoaderWait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(loader)));
		}catch(Exception e) {
			
		}
	}
	
	public void waitForElement(By by) {
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
	}
	
	public void waitForElementToBeClickable(By by) {
		wait.until(ExpectedConditions.elementToBeClickable(by));
	}
	
	public void waitForElementNotToBePresent(By by) {
		try {
			WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(5));
			w.until(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(by)));
		} catch (Exception e) {
		}
		
	}
	
	public void clickElement(By by) {
		waitForElement(by);
		waitForElementToBeClickable(by);
		driver.findElement(by).click();
	}
	
	public void fill(By by, String data) {
		driver.findElement(by).sendKeys(data);
	}
	
	public By dynamicLocator(String tag, String value) {
		return By.xpath("//*[contains(" + tag + ",'" + value + "')]");
	}
	
	public By fieldsInCreateContact(String field) {
		return By.xpath("//*[text()='"+field+"']/parent::*//input");
	}
	
	public String getText(By by) {
		waitForElement(by);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		return driver.findElement(by).getText();
	}
	
	public void scrollPageDown() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,350)", "");
	}
	
	public void scrollPageDown(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}
}
