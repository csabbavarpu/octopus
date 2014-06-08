package framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PortalDashBoardPage extends BasePage{
	
	@FindBy(css = ".authHeaderToolsLogout>a") private WebElement logOut_lnk;
	@FindBy(css = "#authHeaderToolsUser>span") private WebElement loggedUserWelcomeMsg_lbl;
	
	public PortalDashBoardPage(WebDriver webDriver) {
		super(webDriver);
	}
	
	public void logOutFromPortal()
	{
		waitForEleToLoadById("authHeaderToolsUser");
		logOut_lnk.click();
	}
	
	public String getWelComeMsg()
	{
		waitForEleToLoadById("authHeaderToolsUser");
		return loggedUserWelcomeMsg_lbl.getText();
	}

}
