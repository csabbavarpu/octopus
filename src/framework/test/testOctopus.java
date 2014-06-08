package framework.test;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import com.thoughtworks.selenium.DefaultSelenium;

public class testOctopus {
	Logger logger = Logger.getLogger(testOctopus.class);

	
	//---------------------WIP----------------
	private static DefaultSelenium selenium;

	//@Test(testName="googleTest")
	public void startSelenium()
	{
		//selenium = new DefaultSelenium("localhost",4444,"*chrome","http://www.google.com");
		
		selenium = new DefaultSelenium("localhost",4444,"*firefox C:\\Users\\chandra.sabbavarpu\\AppData\\Local\\Mozilla Firefox\\firefox.exe","https://portalval.cms.gov/wps/portal/unauthportal/home/");
//		selenium = new DefaultSelenium("localhost",4444,"*firefox C:\\Users\\chandra.sabbavarpu\\AppData\\Local\\Mozilla Firefox\\firefox.exe","https://eidmt.cms.gov/EIDMLoginApp/login.jsp?authn_try_count=0&contextType=external&username=string&OverrideRetryLimit=3&contextValue=%2Foam&password=sercure_string&challenge_url=https%3A%2F%2Feidmt.cms.gov%2FEIDMLoginApp%2Flogin.jsp&ssoCookie=Secure&request_id=2492225927437235017&locale=en_US&resource_url=https%253A%252F%252Fportalz7.cms.cmstest%252Fwps%252Fmyportal%252Fcmsportal%252Feppe_app%252F%252521ut%252Fp%252Fb1%252FjYu9CoNAEAYfab_dC7fXHoqJhVrkitw1coUEwZ8m5PkTsbKQZLqBGUoUjRNmqAg9KC35PT7za1yXPG2ebN9iR66hqeA71lDqReC2IR4CKUrUhu-N8ZYB-9-PEzx-_fEb6HmgFCjW1N7WeaA5TZVj5z8WlLDU%252Fdl4%252Fd5%252FL2dBISEvZ0FBIS9nQSEh%252Fpw%252FZ7_N00000002GTMF0AO17TD742087%252Fren%252Fp%25253Dwsrp-navigationalState%25253DMTJ2MTExMTRzaG93MTFBMTEyMThyZXNlYXJjaGVyX2R1YUZvcm0*%252F-%252F");
		
		selenium.start();  
        selenium.windowFocus();
		selenium.windowMaximize();
		try{
			selenium.open("https://portalval.cms.gov/wps/portal/unauthportal/home/");
//			selenium.open("https://eidmt.cms.gov/EIDMLoginApp/login.jsp?authn_try_count=0&contextType=external&username=string&OverrideRetryLimit=3&contextValue=%2Foam&password=sercure_string&challenge_url=https%3A%2F%2Feidmt.cms.gov%2FEIDMLoginApp%2Flogin.jsp&ssoCookie=Secure&request_id=2492225927437235017&locale=en_US&resource_url=https%253A%252F%252Fportalz7.cms.cmstest%252Fwps%252Fmyportal%252Fcmsportal%252Feppe_app%252F%252521ut%252Fp%252Fb1%252FjYu9CoNAEAYfab_dC7fXHoqJhVrkitw1coUEwZ8m5PkTsbKQZLqBGUoUjRNmqAg9KC35PT7za1yXPG2ebN9iR66hqeA71lDqReC2IR4CKUrUhu-N8ZYB-9-PEzx-_fEb6HmgFCjW1N7WeaA5TZVj5z8WlLDU%252Fdl4%252Fd5%252FL2dBISEvZ0FBIS9nQSEh%252Fpw%252FZ7_N00000002GTMF0AO17TD742087%252Fren%252Fp%25253Dwsrp-navigationalState%25253DMTJ2MTExMTRzaG93MTFBMTEyMThyZXNlYXJjaGVyX2R1YUZvcm0*%252F-%252F");
			
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}
	@Test(testName="CMSLoginTest",groups = { "functest"} )
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

/*	@Test(testName="SearchTest",groups = { "functest" })
	public void SearchTest()
	{
		startSelenium();
	
		
	}*/
	
	private void waitForElement(String locator) throws InterruptedException
	{
		for (int i = 0; i <5; i++) {
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


}
