package framework.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UserIDRegistrationPage extends BasePage{
	
	@FindBy(xpath = ".//*[@id='pt1:r1:1:userIdTxt::content']") private WebElement usrID_txt;
	@FindBy(xpath = ".//*[@id='pt1:r1:1:passwordTxt::content']") private WebElement passWd_txt;
	@FindBy(xpath = ".//*[@id='pt1:r1:1:confirmPasswordTxt::content']") private WebElement confPwd_txt;
	
	@FindBy(xpath = ".//*[@id='pt1:r1:1:quest1sol::content']") private WebElement selSecQ1;
	@FindBy(xpath = ".//*[@id='pt1:r1:1:quest1solj_id_1::content']") private WebElement selSecQ2;
	@FindBy(xpath = ".//*[@id='pt1:r1:1:quest1solj_id_2::content']") private WebElement selSecQ3;
	
	
	@FindBy(xpath = ".//*[@id='pt1:r1:1:ans1sol::content']") private WebElement secQ1Ans_txt;
	@FindBy(xpath = ".//*[@id='pt1:r1:1:ans1solj_id_1::content']") private WebElement secQ2Ans_txt;
	@FindBy(xpath = ".//*[@id='pt1:r1:1:ans1solj_id_2::content']") private WebElement secQ3Ans_txt;
	
	@FindBy(xpath = ".//*[@id='pt1:r1:1:registerNextButtonNewUser']") private WebElement bttnNext;
	@FindBy(xpath = ".//*[@id='pt1:r1:1:registerCancelButton']") private WebElement bttnCancel;
	
	public UserIDRegistrationPage(WebDriver webDriver) {
		super(webDriver);
	}
	
	public void setUserIDandPassword(String uname, String pwd)
	{
		setText(usrID_txt,uname);
		setText(passWd_txt,pwd);
		setText(confPwd_txt,pwd);
	}
	
	public void selectSecQuestionsAndAnswers() //Temp Hardcoding
	{
		selectItembyTxt(selSecQ1,"What is your favorite radio station?");
		//selectItembyVal(selSecQ1,"0");
		selectItembyTxt(selSecQ2,"What was your favorite toy when you were a child?");
		//selectItembyVal(selSecQ1,"0");
		selectItembyTxt(selSecQ3,"What is your favorite cuisine?");
		//selectItembyVal(selSecQ3,"0");
		//Answers
		setText(secQ1Ans_txt,"YPR");
		setText(secQ2Ans_txt,"Piolt");
		setText(secQ3Ans_txt,"Chicken");
	}
	
	public void createUserIDandPassword(String usrName, String passWd)
	{
		try
		{
		waitForElementToLoad(By.tagName("iframe"),7);
		}catch(TimeoutException e)
		{}
		List<WebElement> frames=webDriver.findElements(By.tagName("iframe"));
		if(frames.size() >0)
		{
			//for (WebElement fname1: frames)
					//System.out.println("frame Name " + fname1.getAttribute("name") + " , Id: " + fname1.getAttribute("id"));
		}
		webDriver.switchTo().defaultContent();
		webDriver.switchTo().frame(0);
		setUserIDandPassword(usrName,passWd);
		selectSecQuestionsAndAnswers();
		//Click Next
		webDriver.switchTo().defaultContent();
		webDriver.switchTo().frame(0);
		bttnNext.click();
		
	}
}
