package framework.log4j;

import org.apache.log4j.AppenderSkeleton;

import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * A custom log appender for combining all log4j events into a single String.
 */
public class AggregateAppender extends AppenderSkeleton {

    public static final String NAME = "AggregateAppender";

    private final StringBuilder builder = new StringBuilder();

    public AggregateAppender() {
        setName(NAME);
        setLayout(new SimpleLayout());
    }

    /**
     * Stores the formatted log event.
     */
    @Override
    protected void append(LoggingEvent event) {
        builder.append(getLayout().format(event));
    }

    public void close() {}

    public boolean requiresLayout() {
        return false;
    }

    /**
     * Gets the complete set of log messages captured.
     * @return A string of all messages combined.
     */
    public String getData() {
        return builder.toString();
    }

    /**
     * Clears all the stored log messages.
     */
    public void clearData() {
        builder.setLength(0);
    }

    /**
     * Returns the size of the logs stored.
     *
     * @return The length of the sequence of characters stored
     */
    public int getDataSize() {
        return builder.length();
    }

}