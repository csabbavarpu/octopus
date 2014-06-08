package framework.testng;


import org.apache.log4j.Logger;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.SkipException;

import framework.annotation.AnnotationHelper;

/**
 * Maintains the critical failure state of a test run. Includes utility methods to
 * detect critical failures and react if one occurs.
 */
public class CriticalTestChecker {

    private static final Logger LOGGER = Logger.getLogger(CriticalTestChecker.class);

    private static Throwable error = null;
    private static SkipException skip = null;

    public static boolean isTriggered() {
        return skip != null;
    }

    /**
     * Verifies if a critical test error has been encountered so far. If it has,
     * the method provided will force skipped.
     * @param method The testNG method to be executed
     * @param result The testNG result object
     */
    public static void verifyStatus(ITestNGMethod method, ITestResult result) throws SkipException {
        if (isTriggered()) {
            if (!method.isAfterSuiteConfiguration()) {
                result.setStatus(ITestResult.SKIP);
                result.setThrowable(skip);
                throw skip;
            }
        }
    }

    /**
     * Checks for the occurrence of a critical failure with the result provided.
     * Should be called after all failure/skipped result scenarios.
     * @param result The testNG result object
     */
    public static void checkResult(ITestResult result) {
        if (AnnotationHelper.hasAnnotation(result, CriticalTest.class)) {
            if (error == null) {
                error = result.getThrowable();
                if (error == null) {
                    error = new Exception("Unknown Error, test was probably skipped");
                }

                skip = new SkipException("Critical test failed, aborting execution");

                LOGGER.error("Error", error);
                LOGGER.error("Abort", skip);
            }
        }
    }

}
