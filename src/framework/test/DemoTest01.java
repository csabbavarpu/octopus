package framework.test;


//import org.junit.Assert;
import static org.testng.Assert.*;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DemoTest01 extends TestBase{
	
	String uname;
	String ssn;
	@BeforeClass
	public void setup()
	{
		uname=getRandomAlphaNumeric(12);
		System.out.println("Generated Random Usr Name : " + uname);
		ssn= "666-" + getRandomNumber(2) + "-" + getRandomNumber(4);
		System.out.println("Generated Random SSN : " + ssn);
	}
	
	@Test
	public void tc001_CreateNewUser()
	{
		
		onHomePage.navigateToRegistrationPage();
		onTermsAndConditionsPage.acceptAndClickNext();
		onRegistrationPage.fillRegistration(uname, "PALTest", "JR", "chandra@valsatech.com",
				ssn,"Addressline1", "Addressline2", 
				"Baltimore", "Maryland", "21075", "", 
				"01-11-1975", "717-111-1111");
		onRegistrationPage.clickNextBttn();
		onCreateUserIDandPasswordPage.createUserIDandPassword(uname, "Password01");
		onRegistrationCompletePage.clickOnOKBttn();
	}
	
	@Test
	public void tc002_LoginTest()
	{
		onHomePage.loadHome(url);  // url is coming from TestBase 
		onHomePage.clickOnLoginToPortal();
		onLoginPage.acceptTermsBeforeLogin();
		onLoginPage.loginAs("EPPEADMIN5", "Eppe5admin");
		assertTrue(onPortalDashBoardPage.getWelComeMsg().contains("Welcome"));
		onPortalDashBoardPage.logOutFromPortal();
	}
}
