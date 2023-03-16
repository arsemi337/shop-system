package logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

public class LoggerLayout extends LayoutBase<ILoggingEvent> {

    public String doLayout(ILoggingEvent event) {
        return (event.getTimeStamp() - event.getLoggerContextVO().getBirthTime()) +
                " " +
                event.getLevel() +
                " [" +
                event.getThreadName() +
                "] " +
                event.getLoggerName() +
                " - " +
                event.getFormattedMessage() +
                CoreConstants.LINE_SEPARATOR;
    }
}
