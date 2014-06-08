package framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage{
	
	//	
		@FindBy(css = "#acceptButton") private WebElement acceptBttn;
		
		@FindBy(id = "input_userID") private WebElement userName_txt;
		@FindBy(id = "input-password") private WebElement passWord_txt;
		
		@FindBy(id = "buttonUserID") private WebElement loginBttn;
		@FindBy(id = "buttonCancel") private WebElement loginCancelBttn;
		

	public LoginPage(WebDriver webDriver) {
		super(webDriver);
	}
	
	public void acceptTermsBeforeLogin()
	{
		waitForEleToLoadById("acceptButton");
		acceptBttn.click();
	}
	
	public void loginAs(String uname, String pwd)
	{
		waitForEleToLoadById("input_userID");
		setText(userName_txt,uname);
		setText(passWord_txt,pwd);
		loginBttn.click();
	}
	
	public Boolean isLoggedIn()
	{
		return webDriver.findElement(By.id("waitForEleToLoadById")).isDisplayed();
	}

}
