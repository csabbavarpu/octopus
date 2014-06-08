/*package framework.testng;

import org.apache.log4j.Logger;
import org.testng.ISuite;
import org.testng.ITestResult;
*//**
 * Primary listener for all ISE test suites.
 *//*
public class TestListener extends MyTestListener  {

    private final Logger LOGGER = Logger.getLogger(TestListener.class);

    @Override
    public void onStart(ISuite suite) {
    super.onStart(suite);
    	
    }
    
    @Override
    public void beforeConfiguration(ITestResult result) {
       // super.beforeConfiguration(result);
    
    }

    @Override
    public void onConfigurationFailure(ITestResult result) {
  
        super.onConfigurationFailure(result);
    }

    @Override
    public void onConfigurationSkip(ITestResult result) {
     
        super.onConfigurationSkip(result);
    }

    @Override
    public void onConfigurationSuccess(ITestResult result) {
     
        super.onConfigurationSuccess(result);
    }

    @Override
    public void onTestStart(ITestResult result) {
       // super.onTestStart(result);
       
    }

    @Override
    public void onTestFailure(ITestResult result) {
      
        super.onTestFailure(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
    
        super.onTestSkipped(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
   
        super.onTestSuccess(result);
    }

    @Override
    public String getProductComponent(ISuite suite) {
        return suite.getParameter("component");
    }


 

    @Override
    public String getTestType(ISuite suite) {
        return suite.getParameter("testtype");
    }

    @Override
    protected String getPropertiesFile() {
        // The "production" property is a special case that must be made available
        // as a system property at launch time to enable. If not specified, the
        // development environment will be assumed.
        String useProduction = System.getProperty("production");
        String propsFile = "isetest-dev.properties";
        if (useProduction != null && useProduction.equals("true")) {
            propsFile = "isetest-prod.properties";
        }
        return propsFile;
    }

	@Override
	public String getProductVersion(ISuite suite) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTestEnvName(ISuite suite) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProduct(ISuite suite) {
		// TODO Auto-generated method stub
		return null;
	}

}
*/