package framework.db;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.DeflaterInputStream;
import org.apache.log4j.Logger;
import framework.util.PropertyLoader;

public class ConnectToMYSQL {
	static Logger s_logger= Logger.getLogger(ConnectToMYSQL.class);

	 static String url = "jdbc:mysql://localhost:3306/";
     static String dbName = "pushy";
     static String driver = "com.mysql.jdbc.Driver";
     static String userName = "root";
     static String password ="";
     

     private static final Logger LOGGER = Logger.getLogger(ConnectToMYSQL.class);
     public static synchronized Connection getConnection() 
     {
    	Connection conn=null;
     try {
     Class.forName(driver).newInstance();
      conn = DriverManager.getConnection(url+dbName,userName,password);
     
     //java.sql.Statement st = conn.createStatement();
  /*   ResultSet res = st.executeQuery("SELECT * FROM  TestCase");
     while (res.next()) {
     String id = res.getString("TestCaseName");
     String msg = res.getString("TestCaseDesc");
     System.out.println(id + "\t" + msg);
     }
*/
     //conn.close();
     } catch (Exception e) {
     e.printStackTrace();
     }
	return conn;
     } 
    // setTestResultId(Database.addTestResult(getTestRunId(), testName, className, isTest, start, end, state));

    public  static String TableName="TestCase";
    public static  Map<String, String> map= new LinkedHashMap<String, String>();
    public   static ArrayList<String> array= new ArrayList<String>();
     
     
    
     /*public static synchronized boolean updateTestResult( String TestCaseName, String TestResult) {
    	 
         String sql = "UPDATE " + TableName +
                 " SET TestCaseName=?, TestCaseDesc=?" ;
         try {
             Connection conn = getConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql);
             stmnt.setString(1,TestCaseName);
             stmnt.setString(2,TestResult);
             stmnt.executeUpdate();
             stmnt.close();
             s_logger.debug("Updated test result : " +TestCaseName);
             
             java.sql.Statement st = conn.createStatement();
             ResultSet res = st.executeQuery("SELECT * FROM  TestCase");
             while (res.next()) {
             String id = res.getString("TestCaseName");
             String msg = res.getString("TestCaseDesc");
             System.out.println(id + "\t" + msg);
     
             map.put(id, msg);
            
             }
             conn.close();
             return true;
             
         } catch (SQLException e) {
             s_logger.error("Failed to update test result : " + TestCaseName, e);
             return  false;
         }
       
     }
     
*/     
  

     
     
     
     
     public static class EmptyTestBlobException extends IOException {
         private static final long serialVersionUID = 1L;

         EmptyTestBlobException(String msg) {
             super(msg);
         }
     }

     /**
      * Gets the current connection to the database. If a connection has not yet
      * been established, it will attempt to make one.
      *
      * @return The database connection object
      */
    /* public static synchronized Connection getConnection() {
         if (connection == null) {
             try {
                 // Only use the production level database when explicitly set in a system property.
                 // We need to protect this host to prevent incomplete/false results from polluting
                 // any reports generated from it.
                 String dbAddr, dbPort, dbName, dbUser, dbPw;
                 if (isProductionModeEnabled()) {
                     dbAddr = PropertyUtils.getProperty("production.db.host");
                     dbPort = PropertyUtils.getProperty("production.db.port");
                     dbName = PropertyUtils.getProperty("production.db.name");
                     dbUser = PropertyUtils.getProperty("production.db.user");
                     dbPw = PropertyUtils.getProperty("production.db.pw");
                 } else {
                     dbAddr = PropertyUtils.getProperty("development.db.host");
                     dbPort = PropertyUtils.getProperty("development.db.port");
                     dbName = PropertyUtils.getProperty("development.db.name");
                     dbUser = PropertyUtils.getProperty("development.db.user");
                     dbPw = PropertyUtils.getProperty("development.db.pw");
                 }

                 Class.forName("com.mysql.jdbc.Driver");
                 String url = "jdbc:mysql://" + dbAddr + ":" + dbPort + "/" + dbName;
                 LOGGER.debug("Connecting to: " + url);
                 connection = DriverManager.getConnection(url, dbUser, dbPw);
                 LOGGER.debug("Connected to database");
             } catch (ClassNotFoundException e) {
                 LOGGER.error("MySQL driver not found", e);
                 throw new RuntimeException(e);
             } catch (SQLException e) {
                 LOGGER.error("Error connecting to DB", e);
                 throw new RuntimeException(e);
             }
         }
         return connection;
     }*/

     /**
      * Adds a record to the database to indicate the start of a test run.
      * 
      * @param product The product under test
      * @param version The version of the product
      * @param component The component of the product under test
      * @param testSuite The name of the test suite be executed
      * @param testType The type of testing being performed (e.g. sanity or regression)
      * @param testEnvName The name of the test environment where the test is running
      * @param trackingId A free form id for tracking test runs before execution
      * @return The id of newly inserted record or -1 if a failure occurred
      * @throws SQLException
      */
     public static synchronized int startTestRun(String product, String version, String component, String testSuite,
             String testType, String testEnvName, String trackingId) throws SQLException {
         framework.db.Constants.TestRunState state = isProductionModeEnabled() ? framework.db.Constants.TestRunState.OFFICIAL : framework.db.Constants.TestRunState.DEVELOPMENT;
         Timestamp now = new Timestamp(System.currentTimeMillis());
         String sql = "INSERT INTO " + Constants.TESTRUNTABLE +
                 " (product, version, component, testsuite, testtype, testenvname, starttime, state, trackingid)" +
                 " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
         try {
             Connection conn = getConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             stmnt.setString(1, product);
             stmnt.setString(2, version);
             stmnt.setString(3, component);
             stmnt.setString(4, testSuite);
             stmnt.setString(5, testType);
             stmnt.setString(6, testEnvName);
             stmnt.setTimestamp(7, now);
             stmnt.setInt(8, state.getValue());
             stmnt.setString(9, trackingId);
             stmnt.executeUpdate();
             int testRunId = getGeneratedId(stmnt);
             stmnt.close();
             LOGGER.debug("Started test run id: " + testRunId);
             return testRunId;
         } catch (SQLException e) {
             LOGGER.error("Failed to start a test run", e);
             throw e;
         }
     }

     /**
      * Marks the current test run as completed in the database.
      * 
      * @param testRunId The id of the current test run
      * @throws SQLException
      */
     public static synchronized void endTestRun(int testRunId) throws SQLException {
         Timestamp now = new Timestamp(System.currentTimeMillis());
         String sql = "UPDATE " + Constants.TESTRUNTABLE + " SET endtime=? WHERE id=?";

         try {
             Connection conn = getConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql);
             stmnt.setTimestamp(1, now);
             stmnt.setInt(2, testRunId);
             stmnt.executeUpdate();
             stmnt.close();
             LOGGER.debug("Ended test run id: " + testRunId);
         } catch (SQLException e) {
             LOGGER.error("Failed to end a test run", e);
             throw e;
         }
     }

     /**
      * Updates a test run record's product version in the database.
      *
      * @param testRunId The id of the current test run
      * @param version The version of the product
      * @return false if a failure occurred
      */
     public static synchronized boolean updateTestRunProductVersion(int testRunId, String version) {
         String sql = "UPDATE " + Constants.TESTRUNTABLE + " SET version=? WHERE id=?";
         try {
             Connection conn = getConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql);
             stmnt.setString(1, version);
             stmnt.setInt(2, testRunId);
             stmnt.executeUpdate();
             stmnt.close();
             LOGGER.debug("Updated product version for test run id: " + testRunId);
             return true;
         } catch (SQLException e) {
             LOGGER.error("Failed to update product version for test run id: " + testRunId, e);
             return false;
         }
     }

     /**
      * Adds a pending record to the database for a testing method.
      * 
      * @param testRunId The id of the current test run
      * @param name The name of the test method
      * @param className The name of the class the test resides in
      * @param isTest Marks if the result is a test rather than a configuration method
      * @param startTime The time when the test began
      * @return The id of newly inserted record or -1 if a failure occurred
      * @throws SQLException
      */
     public static synchronized int addPendingTestResult(int testRunId, String name, String className, boolean isTest,
             Timestamp startTime) throws SQLException {
         String sql = "INSERT INTO " + Constants.TESTRESULTTABLE +
                 " (testrunid, name, class, istest, starttime, state)" +
                 " VALUES (?, ?, ?, ?, ?, ?)";
         try {
             Connection conn = getConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             stmnt.setInt(1, testRunId);
             stmnt.setString(2, name);
             stmnt.setString(3, className);
             stmnt.setBoolean(4, isTest);
             stmnt.setTimestamp(5, startTime);
             stmnt.setInt(6, framework.db.Constants.TestResultState.PENDING.getValue());
             stmnt.executeUpdate();
             int testResultId = getGeneratedId(stmnt);
             stmnt.close();
             LOGGER.debug("Added pending test result id: " + testResultId);
             return testResultId;
         } catch (SQLException e) {
             LOGGER.error("Failed to add a pending test result", e);
             throw e;
         }
     }

     /**
      * Updates a test result record in the database.
      *
      * @param testResultId The id of the current test result
      * @param endTime The time when the test completed
      * @param state The final result state of the test (PASSED,FAILED,etc.)
      * @return false if a failure occurred
      */
     public static synchronized boolean updateTestResult(int testResultId, Timestamp endTime, framework.db.Constants.TestResultState state) {
         String sql = "UPDATE " + Constants.TESTRESULTTABLE +
                 " SET endTime=?, state=?" +
                 " WHERE id=?";
         try {
             Connection conn = getConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql);
             stmnt.setTimestamp(1, endTime);
             stmnt.setInt(2, state.getValue());
             stmnt.setInt(3, testResultId);
             stmnt.executeUpdate();
             stmnt.close();
             LOGGER.debug("Updated test result id: " + testResultId);
             return true;
         } catch (SQLException e) {
             LOGGER.error("Failed to update test result id: " + testResultId, e);
             return false;
         }
     }

     /**
      * Adds a complete record to the database for a testing method.
      * 
      * @param testRunId The id of the current test run
      * @param name The name of the test method
      * @param className The name of the class the test resides in
      * @param isTest Marks if the result is a test rather than a configuration method
      * @param startTime The time when the test began
      * @param endTime The time when the test completed
      * @param state The final result state of the test (PASSED,FAILED,etc.)
      * @return The id of newly inserted record or -1 if a failure occurred
      * @throws SQLException
      */
     public static synchronized int addTestResult(int testRunId, String name, String className, boolean isTest,
             Timestamp startTime, Timestamp endTime, framework.db.Constants.TestResultState state) throws SQLException {
         String sql = "INSERT INTO " + Constants.TESTRESULTTABLE +
                 " (testrunid, name, class, istest, starttime, endtime, state)" +
                 " VALUES (?, ?, ?, ?, ?, ?, ?)";
         try {
             Connection conn = getConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             stmnt.setInt(1, testRunId);
             stmnt.setString(2, name);
             stmnt.setString(3, className);
             stmnt.setBoolean(4, isTest);
             stmnt.setTimestamp(5, startTime);
             stmnt.setTimestamp(6, endTime);
             stmnt.setInt(7, state.getValue());
             stmnt.executeUpdate();
             int testResultId = getGeneratedId(stmnt);
             stmnt.close();
             LOGGER.debug("Added test result id: " + testResultId);
             return testResultId;
         } catch (SQLException e) {
             LOGGER.error("Failed to add a test result", e);
             throw e;
         }
     }

     /**
      * Adds a record for a chunk of data to attach to a test result.
      * 
      * @param testResultId The id of the test result to attach to
      * @param source A description of where this data came from
      * @param filename The filename to assign to the data file
      * @param data A byte array of data
      * @return The id of newly inserted record
      * @throws SQLException
      * @throws EmptyTestBlobException
      */
     public static synchronized int addTestBlob(int testResultId, String source, String filename, byte[] data)
             throws SQLException, EmptyTestBlobException {
         return addTestBlob(testResultId, source, filename, new ByteArrayInputStream(data), data.length);
     }

     /**
      * Adds a record for a chunk of data to attach to a test result.
      * 
      * @param testResultId The id of the test result to attach to
      * @param source A description of where this data came from
      * @param filename The filename to assign to the data file
      * @param file A file object pointing to the data
      * @return The id of newly inserted record
      * @throws FileNotFoundException
      * @throws SQLException
      * @throws EmptyTestBlobException
      */
     public static synchronized int addTestBlob(int testResultId, String source, String filename, File file)
             throws FileNotFoundException, SQLException, EmptyTestBlobException {
         if (file.isDirectory()) {
             LOGGER.error("File cannot be a directory: " + file.getAbsolutePath());
             throw new FileNotFoundException("File cannot be a directory: " + file.getAbsolutePath());
         }
         try {
             return addTestBlob(testResultId, source, filename, new FileInputStream(file), file.length());
         } catch (FileNotFoundException e) {
             LOGGER.error("File not found: " + file.getAbsolutePath());
             throw e;
         }
     }

     /**
      * Adds a record for a chunk of data to attach to a test result.
      * 
      * @param testResultId The id of the test result to attach to
      * @param source A description of where this data came from
      * @param filename The filename to assign to the data file
      * @param is An input stream associated with the data
      * @param size The amount of bytes to read
      * @return The id of newly inserted record
      * @throws SQLException
      * @throws EmptyTestBlobException
      */
     public static synchronized int addTestBlob(int testResultId, String source, String filename, InputStream is,
             long size) throws SQLException, EmptyTestBlobException {

         // Don't attach empty test blobs. This is not necessarily an error but we'll let others decide that.
         if (size == 0) {
             throw new EmptyTestBlobException("Cannot add a test blob of size 0");
         }

         String sql = "INSERT INTO " + Constants.TESTBLOBTABLE +
                 " (testresultid, source, filename, size, data)" +
                 " VALUES (?, ?, ?, ?, ?)";
         try {
             Connection conn = getConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             stmnt.setInt(1, testResultId);
             stmnt.setString(2, source);
             stmnt.setString(3, filename);
             stmnt.setLong(4, size);
             // Compressing the blob reduces the size dramatically.
             stmnt.setBlob(5, new DeflaterInputStream(is), size);
             stmnt.executeUpdate();
             int testBlobId = getGeneratedId(stmnt);
             stmnt.close();
             LOGGER.debug("Added test blob id: " + testBlobId);
             return testBlobId;
         } catch (SQLException e) {
             LOGGER.error("Failed to add a test blob", e);
             throw e;
         }
     }

     /**
      * Retrieves the generated id from an executed SQL statement.
      * 
      * @param stmnt The Statement already executed
      * @return The id found
      * @throws SQLException
      */
     private static int getGeneratedId(Statement stmnt) throws SQLException {
         ResultSet keys = stmnt.getGeneratedKeys();
         keys.next();
         int id = keys.getInt(1);
         keys.close();
         return id;
     }

     /**
      * Checks to see if production mode is enabled.
      *
      * @return true if production mode is enabled, otherwise false
      */
     private static boolean isProductionModeEnabled() {
         String useProduction = PropertyLoader.getProperty("production");
         if (useProduction != null) {
             if (useProduction.equals("true")) {
                 return true;
             }
         }
         return false;
     }
     
     /**
      * Adds a performance summary entry for a test result.
      * 
      * @param testResultId : test result id
      * @param metricID : the id of a valid performance metric
      * @param value : a numerical value for the metric (i.e. AverageRunTime or MinRunTime)
      * @return : unique id of the entry
      * @throws SQLException 
      */
     private static synchronized int addPerfSummary(int testResultId, int metricID, double value) throws SQLException {
         String sql = "INSERT INTO " + Constants.PERFSUMMARYTABLE +
                 " (testresultid, metricid, value)" +
                 " VALUES (?, ?, ?)";
         try {
             Connection conn = getConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             stmnt.setInt(1, testResultId);
             stmnt.setInt(2, metricID);
             stmnt.setDouble(3, value);
             stmnt.executeUpdate();
             int perfSummaryID = getGeneratedId(stmnt);
             stmnt.close();
             LOGGER.debug("Added performance summary. PerfSummary ID = : " + perfSummaryID);
             return perfSummaryID;
         } catch (SQLException e) {
             LOGGER.error("Failed to add performance summary.", e);
             throw e;
         }
     }

     /**
      * Add a metric ID to the PerfMetric table.
      *
      * @param name : the unique identifier of the metric
      * @param description : human readable description of the field
      * @param unit : unit for the data
      * @return : id for the metric entry
      * @throws SQLException 
      */
     private static synchronized int addPerfMetric(String name, String description, String unit) throws SQLException {
         String sql = "INSERT INTO " + Constants.PERFMETRICTABLE +
                 " (name, description, unit)" +
                 " VALUES (?, ?, ?)";
         try {
             Connection conn = getConnection();
         	int metricID = getPerfMetricID(name);
         	if (metricID == -1) {
         		PreparedStatement stmnt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                 stmnt.setString(1, name);
                 stmnt.setString(2, description);
                 stmnt.setString(3, unit);
                 stmnt.executeUpdate();
                 metricID = getGeneratedId(stmnt);
                 stmnt.close();
                 LOGGER.debug("Added performance metric id: " + metricID);
         	}
             return metricID;
         } catch (SQLException e) {
             LOGGER.error("Failed to add performance metric.", e);
             throw e;
         }
     }

     /**
      * Get the metricID from the perfMetric table given the metric name.
      * 
      * @param name: String name to look up the id with.
      * @return : metric id associated with the string, -1 if not found
      * @throws SQLException
      */
     private static synchronized int getPerfMetricID(String name) throws SQLException {
         String sql = "SELECT id FROM " + Constants.PERFMETRICTABLE + " WHERE name=?";
         
         try {
 	        Connection conn = getConnection();
 	        PreparedStatement stmnt = conn.prepareStatement(sql);
 	        stmnt.setString(1, name);
 	        ResultSet res = stmnt.executeQuery();
 	        if (!res.next()) {
 	        	LOGGER.debug ("Metric id not found in perf metric table");
 	        	return -1;
 	        }
 	        int metricID = res.getInt("id");
 	        stmnt.close();
 	        LOGGER.debug("Retrieved performance metric id: " + metricID);
 	        return metricID;
         } catch (SQLException e) {
             LOGGER.error("Failed to get performance metric.", e);
             throw e;
         }

     }

     /**
      * Add performance summary entry for testResultID and specific metric
      * 
      * @param testResultID : test result id
      * @param metricName : the metric identifier as string
      * @param value : performance data for the metric
      * @param description : human readable description of the field
      * @param unit : unit for the data
      * @return : perfSummaryID; the id of the new entry.
      * @throws SQLException
      */
     public static synchronized int addPerfData(int testResultID, String metricName, double value,
             String description, String unit) throws SQLException {
         return addPerfSummary(testResultID, addPerfMetric(metricName, description, unit), value);
     }


}

