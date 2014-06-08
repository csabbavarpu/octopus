/*package framework.testng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.IConfigurationListener;
import org.testng.IConfigurationListener2;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import framework.db.ConnectToMYSQL;
import framework.db.ConnectToMYSQL.EmptyTestBlobException;
import framework.db.Constants.TestResultState;
import framework.email.EmailSender;
import framework.log4j.AggregateAppender;
import framework.util.LoggingUtils;
import framework.util.PropertyLoader;


*//**
 * An abstract TestNG listener that provides the basic requirements for the framework.
 *//*
public abstract class MyTestListener implements ISuiteListener, ITestListener, IConfigurationListener,
        IConfigurationListener2 {

    private static final Logger LOGGER = Logger.getLogger(MyTestListener.class);

    // Store thread local copies of the testrun and testrunid to prevent race conditions when running parallel test
    // cases. Though running parallel tests is not completely supported, this allows them to still run without
    // colliding while accessing these variables.
    private static InheritableThreadLocal<Integer> testRunId = new InheritableThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return -1;
        }
    };

    private static InheritableThreadLocal<Integer> testResultId = new InheritableThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return -1;
        }
    };

    private static boolean writeToDB = true;

    private String beforeSuiteClassName;
    private String afterSuiteClassName;

    *//**
     * Creates a new entry in the database to indicate this suite is running.
     * Also initializes properties for the testing environment.
     *//*
    public void onStart(ISuite suite) {
      try {
            LoggingUtils.initLogging();
        } catch (IOException e) {
            LOGGER.error("Failed to initialize logging", e);
        }

        // Perform some integrity checks against the suite before trying to run anything.
     //   verifySuiteIntegrity(suite);

        // Load properties in the following order (overwriting as it goes):
        // framework project, test project, environment variables, system properties
        try {
            //loadPropertiesFile("env.properties", true);
           // loadPropertiesFile(getPropertiesFile(), true);
            LOGGER.debug("Reading environment variables");
          //  PropertyLoader.loadEnvironmentVariables(true);
            LOGGER.debug("Reading system properties");
            PropertyLoader.loadSystemProperties(true);
           
        } catch (Exception e) {
            LOGGER.error("Failed to load properties file", e);
        }

        // Enable DB writing if a property has been set.
        String archiveEnabled = PropertyLoader.getProperty("archive.enabled");
        if (archiveEnabled != null && archiveEnabled.equals("true")) {
            LOGGER.debug("Database archiving has been enabled");
            writeToDB = true;
        }

        LoggingUtils.enableAggregateLogger(Logger.getRootLogger(), Level.ALL);

        if (writeToDB) {
            try {
                setTestRunId(ConnectToMYSQL.startTestRun(
                        getProduct(suite),
                        getProductVersion(suite),
                        getProductComponent(suite),
                        suite.getName(),
                        getTestType(suite),
                        getTestEnvName(suite),
                        getTrackingId()));
               
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    *//**
     * Writes to the database to indicate this suite is finished.
     * Also sends a notification email if enabled.
     *//*
    public void onFinish(ISuite suite) {
        if (writeToDB) {
            try {
                ConnectToMYSQL.endTestRun(getTestRunId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            sendEmailNotification(suite);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email", e);
        }
    }

    public void beforeConfiguration(ITestResult result) {
        beforeTestOrConfiguration(result);
    }

    *//**
     * Writes a FAILED result to the database.
     *//*
    public void onConfigurationFailure(ITestResult result) {
        addOrUpdateTestResult(result, TestResultState.FAILED);
        //CriticalTestChecker.checkResult(result);
        afterTestOrConfiguration(result);
    }

    *//**
     * Writes a BLOCKED result to the database.
     *//*
    public void onConfigurationSkip(ITestResult result) {
        addOrUpdateTestResult(result, TestResultState.BLOCKED);
        afterTestOrConfiguration(result);
    }

    *//**
     * Writes a PASSED result to the database.
     *//*
    public void onConfigurationSuccess(ITestResult result) {
        addOrUpdateTestResult(result, TestResultState.PASSED);
        afterTestOrConfiguration(result);
    }

    public void onStart(ITestContext context) {}

    public void onFinish(ITestContext context) {}

    public void onTestStart(ITestResult result) {
        beforeTestOrConfiguration(result);
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        afterTestOrConfiguration(result);
    }

    *//**
     * Writes a FAILED result to the database.
     *//*
    public void onTestFailure(ITestResult result) {
        CriticalTestChecker.checkResult(result);
        addOrUpdateTestResult(result, TestResultState.FAILED);
        afterTestOrConfiguration(result);
    }

    *//**
     * Writes a BLOCKED result to the database.
     *//*
    public void onTestSkipped(ITestResult result) {
        CriticalTestChecker.checkResult(result);
        // Unlike configuration methods, if a test is skipped testNG still calls the equivalent before method.
        // This is probably a testNG bug. This means that we have already added a pending state for this result
        // so we need to update it with a skipped/blocked state.
        addOrUpdateTestResult(result, TestResultState.BLOCKED);
        afterTestOrConfiguration(result);
    }

    *//**
     * Writes a PASSED result to the database.
     *//*
    public void onTestSuccess(ITestResult result) {
        addOrUpdateTestResult(result, TestResultState.PASSED);
        afterTestOrConfiguration(result);
    }

    *//**
     * Gets the name of the product under test.
     * @param suite The testNG suite being executed
     * @return The product name
     *//*
    public abstract String getProduct(ISuite suite);

    *//**
     * Gets the component of the product under test.
     * @param suite The testNG suite being executed
     * @return The product component
     *//*
    public abstract String getProductComponent(ISuite suite);

    *//**
     * Gets the version of the product under test.
     * @param suite The testNG suite being executed
     * @return The product version
     *//*
    public abstract String getProductVersion(ISuite suite);

    *//**
     * Gets the name of the test environment in which the suite is executing.
     * @param suite The testNG suite being executed
     * @return The name of the test environment
     *//*
    public abstract String getTestEnvName(ISuite suite);

    *//**
     * Gets the type of testing being performed. E.g. sanity, regression, etc.
     * @param suite The testNG suite being executed
     * @return The test type description
     *//*
    public abstract String getTestType(ISuite suite);

    *//**
     * Returns the resource location of the property file to load.
     * @return The file name
     *//*
    protected abstract String getPropertiesFile();

    *//**
     * Gets the tracking id of the current test run from PropertyLoader.
     * @return The id as a String
     *//*
    private String getTrackingId() {
    	PropertyLoader.setProperty("tracking.id","");
        String id = PropertyLoader.getProperty("tracking.id");
        return id.isEmpty() ? null : id;
    }

    *//**
     * Adds a record for a chunk of data to attach to a test result.
     *//*
    public static void addTestBlob(String source, String filename, byte[] data) {
        if (writeToDB) {
            try {
                ConnectToMYSQL.addTestBlob(getTestResultId(), source, filename, data);
            } catch (EmptyTestBlobException e) {
                LOGGER.warn("Not adding empty test blob: " + filename);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    *//**
     * Adds a record for a chunk of data to attach to a test result.
     *//*
    public static void addTestBlob(String source, String filename, File file) {
        if (writeToDB) {
            try {
                ConnectToMYSQL.addTestBlob(getTestResultId(), source, filename, file);
            } catch (EmptyTestBlobException e) {
                LOGGER.warn("Not adding empty test blob: " + filename);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    *//**
     * Adds a record for a chunk of data to attach to a test result.
     *//*
    public static void addTestBlob(String source, String filename, InputStream is, long size) {
        if (writeToDB) {
            try {
                ConnectToMYSQL.addTestBlob(getTestResultId(), source, filename, is, size);
            } catch (EmptyTestBlobException e) {
                LOGGER.warn("Not adding empty test blob: " + filename);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    *//**
     * Gets the current test run id. If one has not been set, a RuntimeException is thrown.
     * @return The test run id
     *//*
    private static int getTestRunId() {
        int id = testRunId.get();
        if (id < 0) {
            throw new RuntimeException("No test run is available yet for writing");
        }
        return id;
    }

    *//**
     * Sets the current test run id.
     * @param value The new test run id
     *//*
    private static void setTestRunId(int value) {
        testRunId.set(value);
    }

    *//**
     * Gets the current test result id. If one has not been set, a RuntimeException is thrown.
     * @return The result id
     *//*
    private static int getTestResultId() {
        if (!isTestResultIdSet()) {
            throw new RuntimeException("No test/configuration result is available yet for writing");
        }
        return testResultId.get();
    }

    *//**
     * Checks if the test result id has been set to a valid value.
     *
     * @return true if it has, otherwise false
     *//*
    private static boolean isTestResultIdSet() {
        int id = testResultId.get();
        return id < 0 ? false : true;
    }

    *//**
     * Sets the current test result id.
     * @param value The new result id
     *//*
    private static void setTestResultId(int value) {
        testResultId.set(value);
    }

    *//**
     * Updates the product version for the test run record.
     *//*
    protected static void updateTestProductVersion(String version) {
        if (writeToDB) {
            ConnectToMYSQL.updateTestRunProductVersion(getTestRunId(), version);
        }
    }

    *//**
     * Common cleanup to do after every configuration or configuration method is executed.
     *//*
    private void afterTestOrConfiguration(ITestResult result) {
        // Reset the test result id to prevent subsequent tests writing to the wrong result.
        setTestResultId(-1);
    }

    *//**
     * Resets the log message capture and checks for any previous critical failure.
     *//*
    private void beforeTestOrConfiguration(ITestResult result) {
        // Clear any logs in case a critical test failure has been encountered previously.
      //  LoggingUtils.clearAggregateLoggers();
        logTestStart(result);
        CriticalTestChecker.verifyStatus(result.getMethod(), result);
        // This method has not been skipped, add a pending result for it.
         addPendingTestResult(result);
    }

    *//**
     * Adds the pending result of a test/configuration method to the database.
     * @param result The result of the method
     *//*
    private void addPendingTestResult(ITestResult result) {
        if (!writeToDB) {
            return;
        }

        String testName = getTestName(result);
        String className = getClassName(result);
        boolean isTest = result.getMethod().isTest();
        Timestamp start = new Timestamp(result.getStartMillis());
        try {
            setTestResultId(ConnectToMYSQL.addPendingTestResult(getTestRunId(), testName, className, isTest, start));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    *//**
     * Adds the result of a test/configuration method to the database.
     * @param result The result of the method
     *//*
    private void addTestResult(ITestResult result, TestResultState state) {
        logTestResult(result, state);

        if (!writeToDB) {
            return;
        }

        String testName = getTestName(result);
        String className = getClassName(result);
        boolean isTest = result.getMethod().isTest();
        Timestamp start = new Timestamp(result.getStartMillis());
        Timestamp end = new Timestamp(result.getEndMillis());
        try {
            setTestResultId(ConnectToMYSQL.addTestResult(getTestRunId(), testName, className, isTest, start, end, state));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    *//**
     * Updates the result of a test/configuration method in the database. If the result does not exist, it will create
     * a new one.
     *
     * @param result The result of the method
     * @param state The final state of the method
     *//*
    private void addOrUpdateTestResult(ITestResult result, TestResultState state) {
        logTestResult(result, state);

        if (!writeToDB) {
            return;
        }

        // If there isn't a test result to update, simply add a new one instead.
        if (!isTestResultIdSet()) {
            addTestResult(result, state);
            return;
        }

        Timestamp end = new Timestamp(result.getEndMillis());
         ConnectToMYSQL.updateTestResult(getTestResultId(), end, state);

        attachAggregateLogs();
    }

    *//**
     * Gets the human readable name of the test case.
     * @param result The result of the method
     * @return A descriptive string
     *//*
    private String getTestName(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description != null && !description.isEmpty()) {
            testName = description;
        }
        return testName;
    }

    *//**
     * Gets the human readable name of the test class.
     * @param result The result of the method
     * @return A descriptive string
     *//*
    private String getClassName(ITestResult result) {
        ITestNGMethod method = result.getMethod();

        // This is a workaround for the before/after suite methods. Since there may only be one of each, TestNG
        // tends to execute the last one it found in the XML which can cause a mismatch against the first/last test
        // methods which follow or precede. Therefore, the first and last test methods' classes are predetermined so we
        // can set this method's class name to the right value.
        if (method.isBeforeSuiteConfiguration()) {
            return beforeSuiteClassName;
        }

        if (method.isAfterSuiteConfiguration()) {
            return afterSuiteClassName;
        }

        return result.getTestClass().getRealClass().getSimpleName();
    }

    *//**
     * Indicates the start of a test case in the log.
     * @param result The result of the method
     *//*
    private void logTestStart(ITestResult result) {
        String testName = getTestName(result);
        String className = getClassName(result);
        LOGGER.info("-------------------- " + className + "." + testName + " STARTED --------------------");
    }

    *//**
     * Indicates the end of a test case with its final state.
     * @param result The result of the method
     * @param state The final state of the method
     *//*
    private void logTestResult(ITestResult result, TestResultState state) {
        String testName = getTestName(result);
        String className = getClassName(result);
        String msg = "-------------------- " + className + "." + testName + " " + state + " --------------------";
        if (state == TestResultState.FAILED) {
            LOGGER.error("Failure caused by:", result.getThrowable());
            LOGGER.error(msg);
        } else if (state == TestResultState.BLOCKED) {
            LOGGER.warn(msg);
        } else {
            LOGGER.info(msg);
        }
    }

    *//**
     * Loads all testing related properties from the available properties file.
     * @param file The file resource to load
     * @param importAll Set to false to only import those already defined
     * @throws IOException
     *//*
    private void loadPropertiesFile(String file, boolean importAll) throws Exception {
       ClassLoader cl = MyTestListener.class.getClassLoader();
        //String path = cl.getResource(file).getPath();
        InputStream is =  cl.getResourceAsStream(file);
       // LOGGER.debug("Reading properties from: " + path);
        PropertyLoader.load(file);
    }


    *//**
     * Sends an email to indicate the completion of a suite execution.
     * @param suite The suite that just finished
     * @throws MessagingException
     *//*
    private void sendEmailNotification(ISuite suite) throws MessagingException {
        // String emailEnabled = PropertyUtils.getProperty("email.enabled");

      

         String sendTo = "pushirattan@gmail.com";
         String cc = "pushirattan@gmail.com";
         String bcc = "pushirattan@gmail.com";

         if (cc.isEmpty()) cc = null;
         if (bcc.isEmpty()) bcc = null;

         String suiteName = suite.getName();
      

         String product = "Octopus";
         String component = "Sanity";
         String version = "1.0";
         String testType = "Full";
         String testEnvName ="InHouse";
         ArrayList<String> testResults=MyTestListener.fetchTestResults();
         
         String from = "pushirattan@gmail.com";
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
                 suiteName, product, component, version, testType, testEnvName,testResults);

        System.out.println("Sending email notification to: " + sendTo);

         try {
 			EmailSender.sendHtmlMail(sendTo, cc, bcc, from, subject, body);
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
     }
    
    
    *//**
     * Attaches each aggregated logger's data to the database.
     *//*
    private void attachAggregateLogs() {
        for (Logger logger : LoggingUtils.getAggregateLoggers()) {
            String logName;
            if (logger.equals(Logger.getRootLogger())) {
                // The root logger is always the audit log
                logName = LoggingUtils.AUDIT_LOG_NAME;
            } else {
                logName = logger.getName();
                try {
                    // If a class is used for the logger category, try to use the simple name as the filename.
                    logName = Class.forName(logName).getSimpleName();
                } catch (Exception e) {}
            }

            AggregateAppender appender = (AggregateAppender) logger.getAppender(AggregateAppender.NAME);
            // Only attach if there is any data.
            if (appender.getDataSize() > 0) {
                addTestBlob("Framework", logName + ".log", appender.getData().getBytes());
            }
        }
    }

    *//**
     * Ensures the suite follows a certain structure and set of requirements.
     *
     * @param suite The testNG suite being executed
     *//*
    private void verifySuiteIntegrity(ISuite suite) {
        verifyPreserveOrder(suite);
        verifySuiteHasTests(suite);
        //verifyTestClassInheritance(suite);
       // verifyConfigurationMethods(suite);
        setBeforeSuiteClassName(suite);
        setAfterSuiteClassName(suite);
    }

    *//**
     * Enforce TestNG to preserve the order of execution to the order in which the tests are defined in the XML.
     * Order is not maintained inside a test class unless the methods are explicitly included in the XML.
     *
     * @param suite The testNG suite being executed
     *//*
    private void verifyPreserveOrder(ISuite suite) {
        // Check the setting at the suite element level.
        XmlSuite xmlSuite = suite.getXmlSuite();
        if (!xmlSuite.getPreserveOrder().equals("true")) {
            LOGGER.warn("Suite preserve order cannot be disabled, enabling it.");
            xmlSuite.setPreserveOrder("true");
        }

        // Check the setting at each test element level.
        for (XmlTest xmlTest : xmlSuite.getTests()) {
            if (!xmlTest.getPreserveOrder().equals("true")) {
                LOGGER.warn("Test (" + xmlTest.getName() + ") preserve order cannot be disabled, enabling it.");
                xmlTest.setPreserveOrder("true");
            }
        }
    }

   
    *//**
     * Enforces that at least one test method is scheduled to be executed as the result of running this suite. This is
     * useful to prevent before/after suite methods from executing when no tests are defined/enabled.
     *
     * @param suite The testNG suite being executed
     *//*
    private void verifySuiteHasTests(ISuite suite) {
        if (suite.getAllMethods().size() == 0) {
            throw new RuntimeException("At least one test method must be enabled/defined");
        }
    }

    
    
    *//**
     * Enforces that all configuration methods throughout all test classes must be declared in the AbstractTestBase.
     * This ensures that no additional TestNG before/after annotations may be used.
     *
     * @param suite The testNG suite being executed
     *//*
    private void verifyConfigurationMethods(ISuite suite) {
        // Get a mapping of all class names that contain test methods which will run to their ITestClass objects.
        // Note: This is different than the classes that are defined in the suite XML.
        Map<String, ITestClass> testClasses = new HashMap<String, ITestClass>();
        for (ITestNGMethod method : suite.getAllMethods()) {
            ITestClass testClass = method.getTestClass();
            if (!testClasses.containsKey(testClass.getName())) {
                testClasses.put(testClass.getName(), testClass);
            }
        }

        for (String className : testClasses.keySet()) {
            ITestClass testClass = testClasses.get(className);

            // Construct a list of all before/after config methods.
            List<ITestNGMethod> configMethods = new ArrayList<ITestNGMethod>();
            configMethods.addAll(Arrays.asList(testClass.getBeforeSuiteMethods()));
            configMethods.addAll(Arrays.asList(testClass.getBeforeClassMethods()));
            configMethods.addAll(Arrays.asList(testClass.getBeforeTestMethods()));
            configMethods.addAll(Arrays.asList(testClass.getBeforeGroupsMethods()));
            configMethods.addAll(Arrays.asList(testClass.getBeforeTestConfigurationMethods()));
            configMethods.addAll(Arrays.asList(testClass.getAfterSuiteMethods()));
            configMethods.addAll(Arrays.asList(testClass.getAfterClassMethods()));
            configMethods.addAll(Arrays.asList(testClass.getAfterTestMethods()));
            configMethods.addAll(Arrays.asList(testClass.getAfterGroupsMethods()));
            configMethods.addAll(Arrays.asList(testClass.getAfterTestConfigurationMethods()));

            // For each config method, make sure it is declared in AbstractTestBase.
            for (ITestNGMethod configMethod : configMethods) {
                Class<?> clazz = configMethod.getConstructorOrMethod().getMethod().getDeclaringClass();
                if (!clazz.equals(AbstractTestBase.class)) {
                    throw new RuntimeException("Found an unsupported configuration method: "
                            + className + "." + configMethod.getMethodName());
                }
            }
        }
    }

    *//**
     * Tries to determine the first test class that will be executed by inspecting the suite XML.
     *
     * @param suite The testNG suite being executed
     * @throws RuntimeException if the class name could not be determined
     *//*
    private void setBeforeSuiteClassName(ISuite suite) {
        // Iterate over the XML from top to bottom
        for (XmlTest test : suite.getXmlSuite().getTests()) {
            for (XmlClass clazz : test.getClasses()) {

                // For each test class, look at the test methods TestNG knows it will execute and compare. If the
                // method's class matches that of the current XML class, this should be the first class methods will
                // run from.
                for (ITestNGMethod method : suite.getAllMethods()) {
                    if (method.getTestClass().getName().equals(clazz.getName())) {
                        beforeSuiteClassName = method.getTestClass().getRealClass().getSimpleName();
                        return;
                    }
                }
            }
        }
        throw new RuntimeException("Failed to determine first test class name");
    }

    *//**
     * Tries to determine the last test class that will be executed by inspecting the suite XML.
     *
     * @param suite The testNG suite being executed
     * @throws RuntimeException if the class name could not be determined
     *//*
    private void setAfterSuiteClassName(ISuite suite) {
        List<ITestNGMethod> methods = suite.getAllMethods();
        List<XmlTest> tests = suite.getXmlSuite().getTests();

        // Iterate over the XML from bottom to top
        for (int i = tests.size() - 1; i >= 0; i--) {
            List<XmlClass> classes = tests.get(i).getClasses();
            for (int j = classes.size() - 1; j >= 0; j--) {

                // For each test class, look at the test methods TestNG knows it will execute and compare. If the
                // method's class matches that of the current XML class, this should be the last class methods will
                // run from.
                for (ITestNGMethod method : methods) {
                    if (method.getTestClass().getName().equals(classes.get(j).getName())) {
                        afterSuiteClassName = method.getTestClass().getRealClass().getSimpleName();
                        return;
                    }
                }
            }
        }
        throw new RuntimeException("Failed to determine last test class name");
    }
    
    
    
	   public static ArrayList<String> fetchTestResults()
	     {
	    	  ArrayList<String> array= new ArrayList<String>();
	    	   Map<String, String> suiteResult=ConnectToMYSQL.map;
	           
	           for (Entry<String, String> entry : suiteResult.entrySet()) {
	   		    System.out.println("TestCaseName=" + entry.getKey() + ", TestCaseDesc=" + entry.getValue());
	   		    array.add("TestCaseName=" + entry.getKey() + ", TestCaseDesc=" + entry.getValue());
	   		}
	           return array;
	     }
	   

}
*/