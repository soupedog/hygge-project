package hygge.logging.loader;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import hygge.commons.spring.HyggeSpringContext;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;

import java.util.Collections;
import java.util.Map;

/**
 * 与 Spring 默认日志配置保持一致的工具
 *
 * @author Xavier
 * @date 2022/7/3
 * @see LogbackLoggingSystem#loadDefaults(LoggingInitializationContext, LogFile)
 * @since 1.0
 */
public class SpringLoggingSystemDefaultHelper {
    private static final ConfigurationPropertyName LOGGING_LEVEL = ConfigurationPropertyName.of("logging.level");

    private static final Bindable<Map<String, LogLevel>> STRING_LOGLEVEL_MAP = Bindable.mapOf(String.class,
            LogLevel.class);

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
        Binder binder = Binder.get(HyggeSpringContext.getConfigurableEnvironment());
        Map<String, LogLevel> levels = binder.bind(LOGGING_LEVEL, STRING_LOGLEVEL_MAP).orElseGet(Collections::emptyMap);
        for (Map.Entry<String, LogLevel> entry : levels.entrySet()) {
            logger(entry.getKey(), Level.valueOf(entry.getValue().name()));
        }

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
