package hygge.logging.loader;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import hygge.commons.spring.HyggeSpringContext;
import hygge.commons.spring.listener.HyggeSpringContextInitListener;
import hygge.logging.enums.OutputModeEnum;
import hygge.logging.logback.configuration.HyggeLogbackConfiguration;
import hygge.logging.logback.converter.HyggeLogbackEscapeExtendedThrowableProxyConverter;
import hygge.logging.logback.converter.HyggeLogbackEscapeFirstRootCauseThrowableProxyConverter;
import hygge.logging.logback.converter.HyggeLogbackEscapeMessageConverter;
import hygge.logging.logback.converter.HyggeLogbackEscapeThrowableProxyConverter;
import hygge.logging.logback.converter.HyggeLogbackJsonFriendlyExtendedThrowableProxyConverter;
import hygge.logging.logback.converter.HyggeLogbackJsonFriendlyFirstRootCauseThrowableProxyConverter;
import hygge.logging.logback.converter.HyggeLogbackJsonFriendlyMessageConverter;
import hygge.logging.logback.converter.HyggeLogbackJsonFriendlyThrowableProxyConverter;
import hygge.logging.logback.converter.HyggeLogbackTimeStampConverter;
import hygge.utils.logback.impl.HyggeLogbackPatterHelper;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Map;

/**
 * 配置 Logger 对象
 *
 * @author Xavier
 * @date 2022/7/3
 * @since 1.0
 */
public class HyggeLogbackLoaderListener implements Ordered, ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private final Logger log = ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger(HyggeLogbackLoaderListener.class);
    private final Logger logForHyggeSpringContext = ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger(HyggeSpringContextInitListener.class);

    @Override
    public int getOrder() {
        // 设置优先级比日志系统默认值低一点
        return LoggingApplicationListener.DEFAULT_ORDER + 2;
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        HyggeLogbackConfiguration configuration = new HyggeLogbackConfiguration();
        if (configuration.isEnable()) {
            loadLogback(configuration);
            log.info("HyggeLoggerLogback init success");
        }

        // 这里和 HyggeLogbackLoaderListener 其实没关系，只是希望是在日志配置完毕后再输出 HyggeContext
        String logInfo = "HyggeContext init success:" + HyggeSpringContext.toJsonVale();
        logForHyggeSpringContext.info(logInfo);
    }

    protected void loadLogback(HyggeLogbackConfiguration configuration) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        if (configuration.isEnableRootOverride()) {
            LayoutWrappingEncoder<ILoggingEvent> rootEncoder = createEncoder(false, configuration, loggerContext);
            Appender<ILoggingEvent> rootAppender = createAppender(false, loggerContext, configuration, rootEncoder);
            // 重置 rootLogger
            restRootLogger(loggerContext, rootAppender);
        }

        Map<String, Boolean> hyggeScopePathsMap = configuration.getHyggeScopePaths();
        if (hyggeScopePathsMap.isEmpty()) {
            return;
        }

        LayoutWrappingEncoder<ILoggingEvent> hyggeEncoder = createEncoder(true, configuration, loggerContext);
        Appender<ILoggingEvent> hyggeAppender = createAppender(true, loggerContext, configuration, hyggeEncoder);

        // 初始化 hyggeLogger
        for (Map.Entry<String, Boolean> entry : hyggeScopePathsMap.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                Logger logger = loggerContext.getLogger(entry.getKey());
                // 不向上传播日志事件，防止多次打印(例如传递给了 Root)
                logger.setAdditive(false);
                logger.setLevel(logger.getLevel());
                logger.addAppender(hyggeAppender);
            }
        }
    }

    private void restRootLogger(LoggerContext loggerContext, Appender<ILoggingEvent> rootAppender) {
        Logger logger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        logger.detachAppender("CONSOLE");
        logger.addAppender(rootAppender);
    }

    private Appender<ILoggingEvent> createAppender(boolean isHyggeScope, LoggerContext loggerContext, HyggeLogbackConfiguration configuration, LayoutWrappingEncoder<ILoggingEvent> encoder) {
        if (OutputModeEnum.CONSOLE.equals(configuration.getOutputMode())) {
            // 控制台输出
            ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
            consoleAppender.setName(isHyggeScope ? "CONSOLE_HYGGE" : "CONSOLE");
            consoleAppender.setContext(loggerContext);
            consoleAppender.setEncoder(encoder);
            consoleAppender.start();
            return consoleAppender;
        } else {
            // 文件输出
            RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
            rollingFileAppender.setContext(loggerContext);
            rollingFileAppender.setEncoder(encoder);

            SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
            rollingPolicy.setContext(loggerContext);
            rollingPolicy.setMaxFileSize(FileSize.valueOf(configuration.getFileMaxSize()));


            String fileNamePattern;
            if (StringUtils.hasText(configuration.getFileNamePattern())) {
                fileNamePattern = configuration.getFileNamePattern();
            } else {
                fileNamePattern = File.separator + (isHyggeScope ? "hygge" : "root") + "-%d{yyyy-MM-dd}.%i.log";
            }

            rollingPolicy.setFileNamePattern(fileNamePattern);
            rollingPolicy.setParent(rollingFileAppender);
            rollingPolicy.start();

            rollingFileAppender.setRollingPolicy(rollingPolicy);
            rollingFileAppender.start();
            return rollingFileAppender;
        }
    }

    private LayoutWrappingEncoder<ILoggingEvent> createEncoder(boolean isHyggeScope, HyggeLogbackConfiguration configuration, LoggerContext loggerContext) {
        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(loggerContext);

        PatternLayout layout = new PatternLayout();
        layout.setContext(loggerContext);

        HyggeLogbackPatterHelper hyggeLogbackPatterHelper = new HyggeLogbackPatterHelper();

        String finalPatter;

        if (isHyggeScope) {
            if (StringUtils.hasText(configuration.getHyggePattern())) {
                finalPatter = configuration.getHyggePattern();
            } else {
                finalPatter = hyggeLogbackPatterHelper.createPatter(configuration, true);
            }
        } else {
            if (StringUtils.hasText(configuration.getRootPattern())) {
                finalPatter = configuration.getRootPattern();
            } else {
                finalPatter = hyggeLogbackPatterHelper.createPatter(configuration, false);
            }
        }

        layout.setPattern(finalPatter);

        Map<String, String> defaultConverterMap = layout.getDefaultConverterMap();

        if (OutputModeEnum.FILE.equals(configuration.getOutputMode())) {
            defaultConverterMap.put("d", HyggeLogbackTimeStampConverter.class.getName());
            defaultConverterMap.put("date", HyggeLogbackTimeStampConverter.class.getName());
        }

        switch (configuration.getConverterMode()) {
            case ESCAPE:
                defaultConverterMap.put("m", HyggeLogbackEscapeMessageConverter.class.getName());
                defaultConverterMap.put("msg", HyggeLogbackEscapeMessageConverter.class.getName());
                defaultConverterMap.put("message", HyggeLogbackEscapeMessageConverter.class.getName());

                defaultConverterMap.put("ex", HyggeLogbackEscapeThrowableProxyConverter.class.getName());
                defaultConverterMap.put("exception", HyggeLogbackEscapeThrowableProxyConverter.class.getName());
                defaultConverterMap.put("rEx", HyggeLogbackEscapeFirstRootCauseThrowableProxyConverter.class.getName());
                defaultConverterMap.put("rootException", HyggeLogbackEscapeFirstRootCauseThrowableProxyConverter.class.getName());
                defaultConverterMap.put("throwable", HyggeLogbackEscapeThrowableProxyConverter.class.getName());

                defaultConverterMap.put("xEx", HyggeLogbackEscapeExtendedThrowableProxyConverter.class.getName());
                defaultConverterMap.put("xException", HyggeLogbackEscapeExtendedThrowableProxyConverter.class.getName());
                defaultConverterMap.put("xThrowable", HyggeLogbackEscapeExtendedThrowableProxyConverter.class.getName());
                break;
            case JSON_FRIENDLY:
                defaultConverterMap.put("m", HyggeLogbackJsonFriendlyMessageConverter.class.getName());
                defaultConverterMap.put("msg", HyggeLogbackJsonFriendlyMessageConverter.class.getName());
                defaultConverterMap.put("message", HyggeLogbackJsonFriendlyMessageConverter.class.getName());

                defaultConverterMap.put("ex", HyggeLogbackJsonFriendlyThrowableProxyConverter.class.getName());
                defaultConverterMap.put("exception", HyggeLogbackJsonFriendlyThrowableProxyConverter.class.getName());
                defaultConverterMap.put("rEx", HyggeLogbackJsonFriendlyFirstRootCauseThrowableProxyConverter.class.getName());
                defaultConverterMap.put("rootException", HyggeLogbackJsonFriendlyFirstRootCauseThrowableProxyConverter.class.getName());
                defaultConverterMap.put("throwable", HyggeLogbackJsonFriendlyThrowableProxyConverter.class.getName());

                defaultConverterMap.put("xEx", HyggeLogbackJsonFriendlyExtendedThrowableProxyConverter.class.getName());
                defaultConverterMap.put("xException", HyggeLogbackJsonFriendlyExtendedThrowableProxyConverter.class.getName());
                defaultConverterMap.put("xThrowable", HyggeLogbackJsonFriendlyExtendedThrowableProxyConverter.class.getName());
                break;
            default:
        }

        layout.start();

        encoder.setLayout(layout);
        encoder.start();
        return encoder;
    }
}
