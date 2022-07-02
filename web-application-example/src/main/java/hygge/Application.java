package hygge;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Xavier
 * @date 2022/7/2
 * @since 1.0
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        /*
         * 等价于 "SpringApplication.run(Application.class)"
         * */
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.run(args);

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        // 停用旧的
//        stopAndReset(loggerContext);

        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(loggerContext);

        PatternLayout layout = new PatternLayout();
//        layout.getDefaultConverterMap().put("msg", MyConverter.class.getName());
        layout.setContext(loggerContext);
        layout.setPattern("%d{HH:mm:ss.SSS} --------------- [%thread] %-5level %logger{36} - %msg%n");
        layout.start();
        encoder.setLayout(layout);

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setContext(loggerContext);
        rollingFileAppender.setEncoder(encoder);

        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setMaxFileSize(FileSize.valueOf("2KB"));
        rollingPolicy.setFileNamePattern("D:\\var\\log\\mylog-%d{yyyy-MM-dd}.%i.log");
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.start();

        rollingFileAppender.setRollingPolicy(rollingPolicy);
        rollingFileAppender.start();

        // 控制台输出
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setName("CONSOLE");
        consoleAppender.setContext(loggerContext);
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        Logger logger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.INFO);
        logger.addAppender(consoleAppender);
        logger.addAppender(rollingFileAppender);
    }
}
