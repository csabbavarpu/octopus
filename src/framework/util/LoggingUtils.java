package framework.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;

import framework.log4j.AggregateAppender;


/**
 * Contains utility functions for various framework logging tasks.
 */
public class LoggingUtils {

    private static final Logger LOGGER = Logger.getLogger(LoggingUtils.class);

    public static final String DEFAULT_LAYOUT_PATTERN = "%d %p [%c{1}] %m%n%throwable";
    public static final Layout DEFAULT_LAYOUT = new EnhancedPatternLayout(DEFAULT_LAYOUT_PATTERN);
    public static final String AUDIT_LOG_NAME = "audit";
    public static final String FILE_OUTPUT_PATH = "logs/" + AUDIT_LOG_NAME + ".log";

    // A list of all loggers that should not be propagated to the root logger and capture all data logged to them
    // utilizing an AggregateAppender.
    private static final List<Logger> aggregateLoggers = new ArrayList<Logger>();

    /**
     * Initializes logging for the entire framework. Should only be called once by the framework.
     *
     * @throws IOException
     */
    public static void initLogging() throws IOException {
        LogManager.resetConfiguration();
        ConsoleAppender consoleAppender = new ConsoleAppender(DEFAULT_LAYOUT);
        RollingFileAppender fileAppender = new RollingFileAppender(DEFAULT_LAYOUT, FILE_OUTPUT_PATH, false);
        fileAppender.setMaxFileSize("64MB");
        fileAppender.setMaxBackupIndex(3);

        Logger rootLogger = Logger.getRootLogger();
        rootLogger.addAppender(consoleAppender);
        rootLogger.addAppender(fileAppender);
    }

    /**
     * Enables an aggregate logger by name with Level ALL.
     *
     * @param name
     *            The logger's string category
     */
    public static void enableAggregateLogger(String name) {
        enableAggregateLogger(name, Level.ALL);
    }

    /**
     * Enables an aggregate logger by name.
     *
     * @param name
     *            The logger's string category
     * @param level
     *            The log4j level to set on the logger
     */
    public static void enableAggregateLogger(String name, Level level) {
        enableAggregateLogger(Logger.getLogger(name), level);
    }

    /**
     * Enables an aggregate logger by class with Level ALL.
     *
     * @param clazz
     *            The logger's class category
     */
    public static void enableAggregateLogger(Class<?> clazz) {
        enableAggregateLogger(clazz, Level.ALL);
    }

    /**
     * Enables an aggregate logger by class.
     *
     * @param clazz
     *            The logger's class category
     * @param level
     *            The log4j level to set on the logger
     */
    public static void enableAggregateLogger(Class<?> clazz, Level level) {
        enableAggregateLogger(Logger.getLogger(clazz), level);
    }

    /**
     * Enables an aggregate logger by object.
     *
     * @param logger
     *            The logger to enable
     * @param level
     *            The log4j level to set on the logger
     */
    public static void enableAggregateLogger(Logger logger, Level level) {
        if (aggregateLoggers.contains(logger)) {
            LOGGER.debug("Aggregate logger already added: " + logger.getName());
            return;
        }

        logger.setLevel(level);
        AggregateAppender appender = new AggregateAppender();
        appender.setLayout(DEFAULT_LAYOUT);
        logger.addAppender(appender);

        // Add all previously inherited appenders from the root logger.
        addRootLoggerAppenders(logger);

        logger.setAdditivity(false);
        aggregateLoggers.add(logger);
    }

    /**
     * Adds all the appenders attached to the root logger to the logger provided. If the logger provided is also the
     * root logger, this method does nothing.
     *
     * @param logger
     *            The logger to add appenders to
     */
    private static void addRootLoggerAppenders(Logger logger) {
        Logger rootLogger = Logger.getRootLogger();
        if (logger.equals(rootLogger)) {
            return;
        }

        Enumeration<?> rootAppenders = rootLogger.getAllAppenders();
        while (rootAppenders.hasMoreElements()) {
            Appender a = (Appender) rootAppenders.nextElement();
            if (!(a instanceof AggregateAppender)) {
                logger.addAppender(a);
            }
        }

    }

    /**
     * Disables an aggregate logger by name.
     *
     * @param name
     *            The logger's string category
     */
    public static void disableAggregateLogger(String name) {
        disableAggregateLogger(Logger.getLogger(name));
    }

    /**
     * Disables an aggregate logger by class.
     *
     * @param clazz
     *            The logger's string category
     */
    public static void disableAggregateLogger(Class<?> clazz) {
        disableAggregateLogger(Logger.getLogger(clazz));
    }

    /**
     * Disables an aggregate logger by object.
     *
     * @param logger
     *            The logger to disable
     */
    public static void disableAggregateLogger(Logger logger) {
        if (!aggregateLoggers.contains(logger)) {
            LOGGER.debug("Aggregate logger already removed: " + logger.getName());
            return;
        }

        logger.removeAppender(AggregateAppender.NAME);
        removeRootLoggerAppenders(logger);

        // Let this logger propagate to its parents.
        logger.setAdditivity(true);
        aggregateLoggers.remove(logger);
    }

    /**
     * Removes all the appenders attached from the root logger common to the logger provided. If the logger provided is
     * also the root logger, this method does nothing.
     *
     * @param logger
     *            The logger to remove appenders from
     */
    private static void removeRootLoggerAppenders(Logger logger) {
        Logger rootLogger = Logger.getRootLogger();
        if (logger.equals(rootLogger)) {
            return;
        }

        Enumeration<?> usedAppenders = logger.getAllAppenders();
        List<Appender> rootAppenders = Collections.list(Logger.getRootLogger().getAllAppenders());

        while (usedAppenders.hasMoreElements()) {
            Appender tempAppender = (Appender) usedAppenders.nextElement();
            if (rootAppenders.contains(tempAppender)) {
                logger.removeAppender(tempAppender);
            }
        }
    }

    /**
     * Clears the logged buffers from all aggregated loggers.
     */
    public static void clearAggregateLoggers() {
        for (Logger logger : LoggingUtils.getAggregateLoggers()) {
            AggregateAppender appender = (AggregateAppender) logger.getAppender(AggregateAppender.NAME);
            appender.clearData();
        }
    }

    /**
     * Gets all aggregate loggers.
     *
     * @return A list of Loggers
     */
    public static List<Logger> getAggregateLoggers() {
        return aggregateLoggers;
    }

}
