package framework.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegistrationCompletePage extends BasePage{
	
	@FindBy(xpath = ".//*[@id='pt1:r1:2:nextButton']") private WebElement bttnOK;

	public RegistrationCompletePage(WebDriver webDriver) {
		super(webDriver);
	}
	
	public void clickOnOKBttn()
	{
		List<WebElement> frames=webDriver.findElements(By.tagName("iframe"));
		if(frames.size() >0)
		{
			for (WebElement fname1: frames)
					System.out.println("frame Name " + fname1.getAttribute("name") + " , Id: " + fname1.getAttribute("id"));
		}
		waitForPageToReload();
		try
		{
		waitForElementToLoad(By.tagName("iframe"),5);
		}catch(TimeoutException e)
		{}
		webDriver.switchTo().defaultContent();
		webDriver.switchTo().frame(0);
		bttnOK.click();
	}

}
