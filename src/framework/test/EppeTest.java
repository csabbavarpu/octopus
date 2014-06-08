package framework.test;
import org.apache.log4j.Logger;
import org.junit.runners.Suite.SuiteClasses;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class EppeTest {
	Logger logger = Logger.getLogger(EppeTest.class);
	
	 @Test(testName="EppeTest")
	public void testGoogle() {
		try{
			FirefoxDriver driver = new FirefoxDriver();			
			// visit Google Map
			driver.get("https://portalval.cms.gov/wps/myportal");
			
//			//enter "Denver, CO" as search terms
//			WebElement input = driver.findElement(By.id("gbqfq"));
//			input.sendKeys("Denver, CO");		
//			input.sendKeys(Keys.ENTER);	
		
		WebElement input = driver.findElementByClassName("cmsButtonLogin");		
		input.click();
		Thread.sleep(10000);
		WebElement btn= driver.findElementById("acceptButton");
		btn.click();
		Thread.sleep(10000);
		WebElement txt= driver.findElementById("input_userID");
		txt.sendKeys("EPPEBO");
		WebElement pwd= driver.findElementById("input-password");
		pwd.sendKeys("Eppe2user");
		Thread.sleep(10000);
		WebElement lnbtn= driver.findElementById("buttonUserID");
		lnbtn.click();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(true, true);
	}
	
	
	
		/*@Test(testName="facebookTest")
	public void facebook() {
		
		//FirefoxDriver driver = new FirefoxDriver();			
		// visit Google Map
		//driver.get("https://maps.google.com/");
		
		// enter "Denver, CO" as search terms
		//WebElement input = driver.findElement(By.id("gbqfq"));
		//input.sendKeys("Denver, CO");		
		//input.sendKeys(Keys.ENTER);	
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(true, true);
	}

	@Test(testName="microsoftTest")
	public void microsoft() {
		
		//FirefoxDriver driver = new FirefoxDriver();			
		// visit Google Map
		//driver.get("https://maps.google.com/");
		
		// enter "Denver, CO" as search terms
		//WebElement input = driver.findElement(By.id("gbqfq"));
		//input.sendKeys("Denver, CO");		
		//input.sendKeys(Keys.ENTER);	
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(true, true);
	}
	

	@Test(testName="whatsppTest")
	public void whatsapp() {
		
		//FirefoxDriver driver = new FirefoxDriver();			
		// visit Google Map
		//driver.get("https://maps.google.com/");
		
		// enter "Denver, CO" as search terms
		//WebElement input = driver.findElement(By.id("gbqfq"));
		//input.sendKeys("Denver, CO");		
		//input.sendKeys(Keys.ENTER);	
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(false, true);
	}*/
	
	
	 
	 
	 
	 
	 
	 
	 
/*	//Selenium RC Test cases
	private static DefaultSelenium selenium;

	//@Test(testName="googleTest")
	public void startSelenium()
	{
		selenium = new DefaultSelenium("localhost",4444,"*firefox","https://portalval.cms.gov/wps/portal/unauthportal/home/");
		selenium.start();  
        selenium.windowFocus();
		selenium.windowMaximize();
		try{
			selenium.open("https://portalval.cms.gov/wps/portal/unauthportal/home/");
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}

	@Test(testName="CMSLoginTest")
	public void startTest()
	{
		startSelenium();
		try{
			this.waitForElement("//a[@class='cmsButtonLogin']");
			selenium.click("//a[@class='cmsButtonLogin']");		
			this.waitForElement("//input[@id='acceptButton']");
			selenium.click("//input[@id='acceptButton']");
			Thread.sleep(10000);
			selenium.type("//input[@id='input_userID']", "EPPEBO");
			selenium.type("//input[@id='input-password']", "Eppe2user");
			Thread.sleep(10000);
			selenium.click("//input[@id='buttonUserID']");

		}
		catch(Exception e)
		{
			
		}
	}
	
	private void waitForElement(String locator) throws InterruptedException
	{
		for (int i = 0; i < 60; i++) {
			if(selenium.isElementPresent(locator))
			{
				break;
			}
			else{
				Thread.sleep(2000);
				continue;
			}
		}
	}

	
	*/
	
	
//	//---------------------WIP----------------
//	private static DefaultSelenium selenium;
//
//	//@Test(testName="googleTest")
//	public void startSelenium()
//	{
//		selenium = new DefaultSelenium("localhost",4444,"*firefox","http://localhost:8080/bodgeit/");
//		selenium.start();  
//        selenium.windowFocus();
//		selenium.windowMaximize();
//		try{
//			selenium.open("http://localhost:8080/bodgeit/");
//		}catch(Exception e){
//			System.out.println(e.getLocalizedMessage());
//		}
//	}
//
//	@Test(testName="AboutUsTest",groups = { "functest","regressiontest" })
//	public void AboutUsTest()
//	{
//		startSelenium();
//		try{
//			Thread.sleep(2000);
//			this.waitForElement("xpath=/html/body/center/table/tbody/tr[2]/td[2]/a");
//			selenium.click("xpath=//html/body/center/table/tbody/tr[2]/td[2]/a");		
//		}
//		catch(Exception e)
//		{
//			logger.error("Failed to execute test");
//		}
//	}
//	
//	@Test(testName="ContactUsTest",groups = { "functest" })
//	public void ContactUsTest()
//	{
//		//startSelenium();
//		
//		try{
//			Thread.sleep(2000);
//			this.waitForElement("xpath=//html/body/center/table/tbody/tr[2]/td[3]/a");
//			selenium.click("xpath=//html/body/center/table/tbody/tr[2]/td[3]/a");
//			Thread.sleep(10000);
//			// selenium.type("//input[@name='comments']", "Hello");
//			
//			//selenium.type("//input[@id=\"comments\"]", "suman");
//			
//			selenium.type("comments", "Hello");
//			
//			Thread.sleep(2000);
//			selenium.click("//input[@id='submit']");
//
//		}
//		catch(Exception e)
//		{
//			logger.error("Failed to execute test");
//		}
//	}
//	
//	
//	@Test(testName="SearchTest",groups = { "functest" })
//	public void SearchTest()
//	{
//		//startSelenium();
//		try{
//			Thread.sleep(2000);
//			this.waitForElement("xpath=//html/body/center/table/tbody/tr[2]/td[6]/a");
//			selenium.click("xpath=//html/body/center/table/tbody/tr[2]/td[6]/a");
//			Thread.sleep(10000);
//			
//			selenium.type("//input[@name='q']", "searchitem");
//
//			Thread.sleep(2000);
//			selenium.click("//input[@type='submit']");
//
//		}
//		catch(Exception e)
//		{
//			logger.error("Failed to execute test");
//		}
//		
//	}
//	
//	private void waitForElement(String locator) throws InterruptedException
//	{
//		for (int i = 0; i <5; i++) {
//			if(selenium.isElementPresent(locator))
//			{
//				break;
//			}
//			else{
//				Thread.sleep(2000);
//				continue;
//			}
//		}
	}


//}
