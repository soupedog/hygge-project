package hygge.logging.loader;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;

/**
 * 与 Spring 默认日志配置保持一致的工具
 *
 * @author Xavier
 * @date 2022/7/3
 * @see LogbackLoggingSystem#loadDefaults(LoggingInitializationContext, LogFile)
 * @since 1.0
 */
public class SpringLoggingSystemDefaultHelper {
    private LoggerContext loggerContext;

    public SpringLoggingSystemDefaultHelper(LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
    }

    void logger(String name, Level level) {
        logger(name, level, true);
    }

    void logger(String name, Level level, boolean additive) {
        logger(name, level, additive, null);
    }

    void logger(String name, Level level, boolean additive, Appender<ILoggingEvent> appender) {
        Logger logger = this.loggerContext.getLogger(name);
        if (level != null) {
            logger.setLevel(level);
        }
        logger.setAdditive(additive);
        if (appender != null) {
            logger.addAppender(appender);
        }
    }

    public void apply() {
        logger("org.apache.catalina.startup.DigesterFactory", Level.ERROR);
        logger("org.apache.catalina.util.LifecycleBase", Level.ERROR);
        logger("org.apache.coyote.http11.Http11NioProtocol", Level.WARN);
        logger("org.apache.sshd.common.util.SecurityUtils", Level.WARN);
        logger("org.apache.tomcat.util.net.NioSelectorPool", Level.WARN);
        logger("org.eclipse.jetty.util.component.AbstractLifeCycle", Level.ERROR);
        logger("org.hibernate.validator.internal.util.Version", Level.WARN);
        logger("org.springframework.boot.actuate.endpoint.jmx", Level.WARN);
    }
}
