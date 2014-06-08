package framework.db;


/**
 * Contains database related constant values.
 */
public class Constants {
    static final String TESTRUNTABLE     = "TestRun";
    static final String TESTRESULTTABLE  = "TestResult";
    static final String TESTBLOBTABLE    = "TestBlob";
    static final String PERFSUMMARYTABLE = "PerfSummary";
    static final String PERFMETRICTABLE  = "PerfMetric";

    /**
     * Possible test run states.
     */
    public static enum TestRunState {
        OFFICIAL(1),
        DEVELOPMENT(2),
        RECYCLED(3);

        private final int value;

        TestRunState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Possible test result states
     */
    public static enum TestResultState {
        PASSED(1),
        FAILED(2),
        BLOCKED(3),
        PENDING(4);

        private final int value;

        TestResultState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
