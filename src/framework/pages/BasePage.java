package framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.seleniumemulation.WaitForPageToLoad;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.thoughtworks.selenium.Selenium;

public class BasePage {
	
	protected WebDriver webDriver;
	protected Selenium selenium;
	//public Logger log = Logger.getLogger(this.getClass());

	/*
	 * Constructor injecting the WebDriver/Selenium interface
	 * 
	 * @param webDriver
	 */
	
	public BasePage(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}
	
	public void setText(final WebElement inEle, final String txt){
		inEle.clear();
		inEle.sendKeys(txt);
	}
	
	public void setText(final WebElement inEle, int txt){
		inEle.clear();
		inEle.sendKeys(String.valueOf(txt));
	}
	
	public void selectItembyTxt(final WebElement selEle, final String val)
	{
		Select sel=new Select(selEle);
		sel.selectByVisibleText(val);
	}
	
	public void selectItembyVal(final WebElement selEle, final String val)
	{
		Select sel=new Select(selEle);
		sel.selectByValue(val);
	}
	
	protected void waitForPageToReload() {
		new WaitForPageToLoad().apply(getWebDriver(), new String[]{Integer.toString(5000)});
	}
	
	
	public void waitForEleToLoadById(final String id)
	{
		waitForElementToLoad(By.id(id),60);
	}

	public void waitForElementToLoad(final By locator, int timeOutInsec)
	{
	new WebDriverWait(webDriver, timeOutInsec).until(new ExpectedCondition<Boolean>(){
		public Boolean apply(WebDriver driver){
			return webDriver.findElement(locator).isDisplayed();
		}
	});
	
	}	
}