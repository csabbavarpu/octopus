package framework.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TermsAndConditions extends BasePage{
	
		//Name
	@FindBy(xpath = ".//*[@id='sbc1']//input") private WebElement accept_check;
	@FindBy(css = "#tcNextButton") private WebElement nextBttn;
	@FindBy(id = "tcCancelButton") private WebElement cancelBttn;

	public TermsAndConditions(WebDriver webDriver) {
		super(webDriver);
	}
	
	public void acceptAndClickNext()
	{
		waitForEleToLoadById("app");
		List<WebElement> frames=webDriver.findElements(By.tagName("iframe"));
		if(frames.size() >0)
		{
			for (WebElement fname: frames)
					System.out.println("frame id " + fname.getAttribute("name") + " , Id: " + fname.getAttribute("id"));
		}
		webDriver.switchTo().frame("app");
		waitForEleToLoadById("TAndCpgl");
		accept_check.click();
		webDriver.switchTo().defaultContent();
		List<WebElement> frames2=webDriver.findElements(By.tagName("iframe"));
		if(frames2.size() >0)
		{
			for (WebElement fname: frames2)
					System.out.println("frame id " + fname.getAttribute("name") + " , Id: " + fname.getAttribute("id"));
		}
		waitForPageToReload();
		webDriver.switchTo().defaultContent();
		webDriver.switchTo().frame(0);
		nextBttn.click();
	}

}
