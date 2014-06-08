package framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage{

	//New User Registration
	@FindBy(xpath = ".//a[contains(@title,\"New User Registration\")]") private WebElement newUserRegistration_link;
	
	//Login
	@FindBy(css = ".cmsButtonLogin") private WebElement loginToPortal_link;
	
	@FindBy(xpath = ".//a[contains(@title,\"Forgot Password\")]") private WebElement forgotPassword_link;
	@FindBy(xpath = ".//a[contains(@title,\"Forgot User ID\")]") private WebElement forgotuserID_link;
	
	
	
	public HomePage(WebDriver webDriver) {
		super(webDriver);
	}

	public void navigateToRegistrationPage()
	{
		newUserRegistration_link.click();
	}
	
	public void navigateToForgotUserID()
	{
		forgotuserID_link.click();	
	}
	
	public void clickOnLoginToPortal()
	{
		waitForElementToLoad(By.cssSelector(".cmsButtonLogin"),60);
		loginToPortal_link.click();
	}
	
	public void loadHome(String url)
	{
		webDriver.navigate().to(url);
		
		
	}

	
	
}
