package framework.pages;

import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegistrationPage extends BasePage{

	//Name
	@FindBy(id = "pt1:r1:0:firstNameTxt::content") private WebElement firstName_txt;
	@FindBy(id = "pt1:r1:0:lastTxt::content") private WebElement lastName_txt;
	@FindBy(id = "pt1:r1:0:suffixSel::content") private WebElement selectSuffix;
	//Email
	@FindBy(id = "pt1:r1:0:emailTxt::content") private WebElement emailId_txt;
	@FindBy(id = "pt1:r1:0:emailConfTxt::content") private WebElement emailIdConfirm_txt;
	
	//SSN
	@FindBy(id = "pt1:r1:0:ssn1Txt::content") private WebElement ssn1_txt;
	@FindBy(id = "pt1:r1:0:ssn2Txt::content") private WebElement ssn2_txt;
	@FindBy(id = "pt1:r1:0:ssn3Txt::content") private WebElement ssn3_txt;
	
	//Date of Brith
	@FindBy(id = "pt1:r1:0:monthSel::content") private WebElement month_txt;
	@FindBy(id = "pt1:r1:0:dayTxt::content") private WebElement date_txt;
	@FindBy(id = "pt1:r1:0:yearTxt::content") private WebElement year_txt;
	
	//Address
	@FindBy(id = "pt1:r1:0:streetTxt::content") private WebElement street1_txt;
	@FindBy(id = "pt1:r1:0:street2Txt::content") private WebElement street2_txt;
	@FindBy(id = "pt1:r1:0:cityTxt::content") private WebElement city_txt;
	@FindBy(id = "pt1:r1:0:stateSel::content") private WebElement select_State;
	@FindBy(id = "pt1:r1:0:zipTxt::content") private WebElement zip_txt;
	@FindBy(id = "pt1:r1:0:zip2Txt::content") private WebElement zip2_txt;
	
	//Phone Number
	@FindBy(id = "pt1:r1:0:phone1Txt::content") private WebElement phone1_txt;
	@FindBy(id = "pt1:r1:0:phone2Txt::content") private WebElement phone2_txt;
	@FindBy(id = "pt1:r1:0:phone3Txt::content") private WebElement phone3_txt;
	
	//Buttons
	@FindBy(id = "pt1:r1:0:infoNextButton") private WebElement bttnNext;
	@FindBy(id = "pt1:r1:0:infoCancelButton") private WebElement bttnCancel;
	
	//User id
	
	//pasword
	
	//Confirm Pasword
	
	//Sec questions
	
	public RegistrationPage(WebDriver webDriver) {
		super(webDriver);
	}
	
	public void set_name(String fname, String lname, String suffix)
	{
		setText(firstName_txt,fname);
		setText(lastName_txt,lname);
		selectItembyTxt(selectSuffix,suffix);
	}
	
	public void set_ssn(String ssn1, String ssn2, String ssn3){
		setText(ssn1_txt,ssn1);
		setText(ssn2_txt,ssn2);
		setText(ssn3_txt,ssn3);
	}
	
	public void set_email(String emailId)
	{
		setText(emailId_txt, emailId);
		setText(emailIdConfirm_txt, emailId);
	}
	
	public void set_address(String line1, String line2, String city, String state, String zip1, String zip2)
	{
		setText(street1_txt,line1);
		setText(street2_txt,line2);
		setText(city_txt,city);
		selectItembyTxt(select_State,state);
		setText(zip_txt,zip1);
		setText(zip2_txt,zip2);
		
	}
	
	public void set_phoneNumber(String phNum1,String phNum2,String phNum3){
		setText(phone1_txt,phNum1);
		setText(phone2_txt,phNum2);
		setText(phone3_txt,phNum3);
	}
	
	public void set_DateOfBirth(int m, int d, int y)
	{
		setText(month_txt,m);
		setText(date_txt,d);
		setText(year_txt,y);
	}
	
	public void fillRegistration(String fname, String lname, String suffix,String emailId,String ssn
			,String line1, String line2, String city, String state, String zip1, String zip2, 
			String dateOfBirth, String phNumber)
	{

		List<WebElement> frames=webDriver.findElements(By.tagName("iframe"));
		if(frames.size() >0)
		{
			for (WebElement fname1: frames)
					System.out.println("frame id " + fname1.getAttribute("name") + " , Id: " + fname1.getAttribute("id"));
		}
		webDriver.switchTo().defaultContent();
		webDriver.switchTo().frame(0);
		Date dt = null;
		
		set_name(fname, lname, suffix);
		set_email(emailId);
		set_ssn(ssn.split("-")[0],ssn.split("-")[1],ssn.split("-")[2]);
		set_phoneNumber(phNumber.split("-")[0],phNumber.split("-")[0],phNumber.split("-")[2]);
		set_address(line1, line2, city, state, zip1, zip2);

		try {
			dt = new SimpleDateFormat("dd-MM-YYYY").parse(dateOfBirth);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal= Calendar.getInstance();
		cal.setTime(dt);
		set_DateOfBirth(cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));		
	}
	
	public void clickNextBttn()
	{
		//webDriver.switchTo().frame(0);
		bttnNext.click();
	}
	

}
