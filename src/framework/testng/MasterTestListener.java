package framework.testng;
import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import framework.db.ConnectToMYSQL;
import framework.db.Constants;
import framework.db.Database;
import framework.email.EmailSender;
import framework.util.LoggingUtils;


public class MasterTestListener implements ISuiteListener, ITestListener {
	IAnnotationTransformer at;

    private static final Logger LOGGER = Logger.getLogger(MasterTestListener.class);

    

	@Override
	public void onTestFailure(ITestResult tr)
	{
		
	    System.out.println("Log Failed tests");
	    Database.updateTestResult(tr.getName(),"FAILED");
		try {
			Database.endTestResult(tr.getName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	 }

	@Override
	public void onTestSuccess(ITestResult tr)
	{
	    System.out.println("Log Passed tests");
	    Database.updateTestResult(tr.getName(),"PASSED");
		try {
			Database.endTestResult(tr.getName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void onTestSkipped(ITestResult tr)
	{
	    System.out.println("Log Passed tests");
	    Database.updateTestResult(tr.getName(),"Skipped");
		try {
			Database.endTestResult(tr.getTestName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	 @Override
	public void onFinish(ISuite suite) {
        try {
        	System.out.println("Set suite resutls");
            sendEmailNotificationNew(suite);
            System.out.println("Emailed test results");
        } catch (MessagingException e) {
        	e.printStackTrace();
            System.out.println("Failed to send email");
        }
    }
    
   
	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		Database.startTestResult(result.getName(),"PENDING");
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
	/*	try {
			Database.endTestResult();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

	@Override
	public void onStart(ISuite suite) {
		// TODO Auto-generated method stub
		
		   try {
	            LoggingUtils.initLogging();
	        } catch (IOException e) {
	            LOGGER.error("Failed to initialize logging", e);
	        }
		Database.startSuiteResult(suite.getName());
		Database.generateUniqueIDForTestRun();
	}

	   public static ArrayList<String> fetchTestResults()
	     {
	    	  ArrayList<String> array= new ArrayList<String>();
	    	   Map<String, String> suiteResult=Database.map;
	           
	           for (Entry<String, String> entry : suiteResult.entrySet()) {
	   		    System.out.println("TestCaseName=" + entry.getKey() + ", TestCaseDesc=" + entry.getValue());
	   		    array.add("TestCaseName=" + entry.getKey() + ", TestCaseDesc=" + entry.getValue());
	   		}
	           return array;
	     }
	   
	   /**
	     * Gets the human readable name of the test case.
	     * @param result The result of the method
	     * @return A descriptive string
	     */
	    private String getTestName(ITestResult result) {
	        String testName = result.getMethod().getMethodName();
	        String description = result.getMethod().getDescription();
	        if (description != null && !description.isEmpty()) {
	            testName = description;
	        }
	        return testName;
	    }

	    private void sendEmailNotification(ISuite suite) throws MessagingException {
	        // String emailEnabled = PropertyUtils.getProperty("email.enabled");

	      

	         String sendTo = "chandra@valsatech.com";
	         String cc = "chandra@valsatech.com";
	         String bcc = "chandra@valsatech.com";

	         if (cc.isEmpty()) cc = null;
	         if (bcc.isEmpty()) bcc = null;

	         String suiteName = suite.getName();
	      

	         String product = "Octopus";
	         String component = "Sanity";
	         String version = "1.0";
	         String testType = "Full";
	         String testEnvName ="InHouse";
	         String TestSuiteResults = null;
	       //  ArrayList<String> testResults=MasterTestListener.fetchTestResults();
	         
	         try {
				TestSuiteResults=Database.FinalTestResult();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	         String from = "chandra@valsatech.com";
	         String subject = "Automation Finished - " + suiteName;
	         String body = String.format(
	                 "<div>The test run is now complete.</div><br />" +
	                 "<div>" +
	                 "<div>Suite: %s</div>" +
	                 "<div>Product: %s</div>" +
	                 "<div>Component: %s</div>" +
	                 "<div>Version: %s</div>" +
	                 "<div>Test Type: %s</div>" +
	                 "<div>Test Environment: %s</div>" +
	                 "<div>Test Results: %s</div>" +
	                 "</div>",
	                 suiteName, product, component, version, testType, testEnvName,TestSuiteResults);

	        System.out.println("Sending email notification to: " + sendTo);

	         try {
	 			EmailSender.sendHtmlMail(sendTo, cc, bcc, from, subject, body);
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	     }

	    
	    
	    private void sendEmailNotificationNew(ISuite suite) throws MessagingException {
	        // String emailEnabled = PropertyUtils.getProperty("email.enabled");

	      

	         String sendTo = "chandra@valsatech.com";
	         String cc = "chandra@valsatech.com";
	         String bcc = "chandra@valsatech.com";

	         if (cc.isEmpty()) cc = null;
	         if (bcc.isEmpty()) bcc = null;

	         String suiteName = suite.getName();
	      

	         String product = "Octopus";
	         String component = "Sanity";
	         String version = "1.0";
	         String testType = "Full";
	         String testEnvName ="InHouse";
	         String TestSuiteResults = null;
	       //  ArrayList<String> testResults=MasterTestListener.fetchTestResults();
	         
	         try {
				TestSuiteResults=Database.FinalTestResultNew();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	         String from = "chandra@valsatech.com";
	         String subject = "Automation Finished - " + suiteName;
	         String body = String.format(
	                 "<div>The test run is now complete.</div><br />" +
	                 "<div>" +
	                 "<div>Suite: %s</div>" +
	                 "<div>Product: %s</div>" +
	                 "<div>Component: %s</div>" +
	                 "<div>Version: %s</div>" +
	                 "<div>Test Type: %s</div>" +
	                 "<div>Test Environment: %s</div>" +
	                 "<div>Test Results: %s</div>" +
	                 "</div>",
	                 suiteName, product, component, version, testType, testEnvName,TestSuiteResults);
	         
	 
	        System.out.println("Sending email notification to: " + sendTo);

	         try {
	 			EmailSender.sendHtmlMail(sendTo, cc, bcc, from, subject, body);
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	     }

	    

	}