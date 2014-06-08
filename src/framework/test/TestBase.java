package framework.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import framework.pages.HomePage;
import framework.pages.LoginPage;
import framework.pages.PortalDashBoardPage;
import framework.pages.RegistrationCompletePage;
import framework.pages.RegistrationPage;
import framework.pages.TermsAndConditions;
import framework.pages.UserIDRegistrationPage;

public class TestBase {
	
	RegistrationPage onRegistrationPage;
	HomePage onHomePage;
	TermsAndConditions onTermsAndConditionsPage;
	LoginPage onLoginPage;
	PortalDashBoardPage onPortalDashBoardPage;
	UserIDRegistrationPage onCreateUserIDandPasswordPage;
	RegistrationCompletePage onRegistrationCompletePage;
	WebDriver driver;
	private static final String ALPHA_NUM ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final String NUM ="0123456789";
	public static String url;
	
	@BeforeSuite
	public void setBeforeSuite(){
		System.setProperty("webdriver.firefox.bin", 
				"C:\\Users\\chandra.sabbavarpu\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
		driver=new FirefoxDriver();
	}
	
	@BeforeClass(alwaysRun=true)
	public void beforeClassSetup()
	{
		url ="https://portalz7.cms.cmstest/wps/portal";
		driver.navigate().to(url);
		

		//CMS DEV Environment with Cisco VPN access
//		driver.navigate().to("https://portalz7.cms.cmstest/wps/portal");
		
		//CMS VAL Environment with Cisco VPN access
//		driver.navigate().to("https://portalz7.cms.cmsval/");
		
		//CMS VAL public internet site
//		url ="https://portalval.cms.gov/wps/portal/";
		
		driver.manage().window().maximize();
		onRegistrationPage = PageFactory.initElements(driver, RegistrationPage.class);
		onHomePage =PageFactory.initElements(driver, HomePage.class);
		onTermsAndConditionsPage = PageFactory.initElements(driver, TermsAndConditions.class);
		onLoginPage=PageFactory.initElements(driver, LoginPage.class);
		onPortalDashBoardPage = PageFactory.initElements(driver, PortalDashBoardPage.class);
		onCreateUserIDandPasswordPage = PageFactory.initElements(driver, UserIDRegistrationPage.class);
		onRegistrationCompletePage = PageFactory.initElements(driver, RegistrationCompletePage.class);
	}
	
	
	public String getRandomAlphaNumeric(int len) {
	      StringBuffer sb = new StringBuffer(len);
	      for (int i=0;  i<len;  i++) {
	         int ndx = (int)(Math.random()*ALPHA_NUM.length());
	         sb.append(ALPHA_NUM.charAt(ndx));
	      }
	      return sb.toString();
	   }
	
	public String getRandomNumber(int len) {
	      StringBuffer sb = new StringBuffer(len);
	      for (int i=0;  i<len;  i++) {
	         int ndx = (int)(Math.random()*NUM.length());
	         sb.append(NUM.charAt(ndx));
	      }
	      return sb.toString();
	   }
	
	@AfterClass(alwaysRun=true)
	public void afterClass()
	{
		driver.close();
	}
	
	@AfterSuite
	public void afterSuite(){
		
	}

}
