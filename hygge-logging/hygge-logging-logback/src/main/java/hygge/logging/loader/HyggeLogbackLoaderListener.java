/*
 * Copyright 2022-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import hygge.logging.util.HyggeLogbackPatterHelper;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 配置 Logger 对象
 *
 * @author Xavier
 * @date 2022/7/3
 * @since 1.0
 */
public class HyggeLogbackLoaderListener implements Ordered, ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private static final Logger log = ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger(HyggeLogbackLoaderListener.class);
    /**
     * 此对象和 HyggeLogbackLoaderListener 其实没关系，只是希望是在日志配置完毕后再输出 HyggeContext 内容，故放在此处
     */
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

        String logInfo = "HyggeContext init success:" + HyggeSpringContext.toJsonVale();
        logForHyggeSpringContext.info(logInfo);
    }

    protected void loadLogback(HyggeLogbackConfiguration configuration) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        if (configuration.isEnableRootOverride()) {
            List<Appender<ILoggingEvent>> rootAppenderList = createAppenderList(false, loggerContext, configuration);

            // 重置 rootLogger
            restRootLogger(loggerContext, rootAppenderList);
        }

        Map<String, Boolean> hyggeScopePathsMap = configuration.getHyggeScopePaths();
        // 不存在 hygge 目录定义时直接跳过初始化
        if (hyggeScopePathsMap.isEmpty()) {
            return;
        }

        List<Appender<ILoggingEvent>> hyggeAppenderList = createAppenderList(true, loggerContext, configuration);

        // 初始化 hyggeLogger
        for (Map.Entry<String, Boolean> entry : hyggeScopePathsMap.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                Logger logger = loggerContext.getLogger(entry.getKey());
                // 不向上传播日志事件，防止多次打印(例如传递给了 Root)
                logger.setAdditive(false);
                logger.setLevel(logger.getLevel());

                for (Appender<ILoggingEvent> hyggeAppender : hyggeAppenderList) {
                    logger.addAppender(hyggeAppender);
                }
            }
        }
    }

    private void restRootLogger(LoggerContext loggerContext, List<Appender<ILoggingEvent>> rootAppenderList) {
        Logger logger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        // 剔除默认的 appender
        logger.detachAppender("CONSOLE");

        for (Appender<ILoggingEvent> appender : rootAppenderList) {
            logger.addAppender(appender);
        }
    }

    /**
     * 创建所需的全部 Appender
     */
    private List<Appender<ILoggingEvent>> createAppenderList(boolean isHyggeScope, LoggerContext loggerContext, HyggeLogbackConfiguration configuration) {
        List<Appender<ILoggingEvent>> result = new ArrayList<>();

        OutputModeEnum outputMode = configuration.getOutputMode();

        // 包含控制台输出模式时，往结果集里添加控制台 Appender
        if (OutputModeEnum.CONSOLE.equals(outputMode) || OutputModeEnum.CONSOLE_AND_FILE.equals(outputMode)) {
            LayoutWrappingEncoder<ILoggingEvent> consoleEncoder = createEncoder(configuration, isHyggeScope, configuration.getEnableColorfulConsole(), OutputModeEnum.CONSOLE, loggerContext);
            result.add(createConsoleAppender(isHyggeScope, loggerContext, consoleEncoder));
        }

        // 包含控制台输出模式时，往结果集里添加文件输出 Appender
        if (OutputModeEnum.FILE.equals(outputMode) || OutputModeEnum.CONSOLE_AND_FILE.equals(outputMode)) {
            LayoutWrappingEncoder<ILoggingEvent> fileEncoder = createEncoder(configuration, isHyggeScope, configuration.getEnableColorfulFile(), OutputModeEnum.FILE, loggerContext);
            result.add(createFileAppender(isHyggeScope, loggerContext, configuration, fileEncoder));
        }

        return result;
    }

    private ConsoleAppender<ILoggingEvent> createConsoleAppender(boolean isHyggeScope, LoggerContext loggerContext, LayoutWrappingEncoder<ILoggingEvent> encoder) {
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setName(isHyggeScope ? "CONSOLE_HYGGE" : "CONSOLE");
        consoleAppender.setContext(loggerContext);
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();
        return consoleAppender;
    }

    private RollingFileAppender<ILoggingEvent> createFileAppender(boolean isHyggeScope, LoggerContext loggerContext, HyggeLogbackConfiguration configuration, LayoutWrappingEncoder<ILoggingEvent> encoder) {
        // 文件输出
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setContext(loggerContext);
        rollingFileAppender.setEncoder(encoder);

        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setMaxFileSize(FileSize.valueOf(configuration.getFileMaxSize()));
        rollingPolicy.setMaxHistory(configuration.getFileMaxHistory());

        String fileNamePattern;
        if (isHyggeScope) {
            if (StringUtils.hasText(configuration.getFileNamePatternHygge())) {
                fileNamePattern = configuration.getFileNamePatternHygge();
            } else {
                fileNamePattern = File.separator + "hygge-%d{yyyy-MM-dd}.%i.log";
                configuration.setFileNamePatternHygge(fileNamePattern);
            }
        } else {
            if (StringUtils.hasText(configuration.getFileNamePatternRoot())) {
                fileNamePattern = configuration.getFileNamePatternRoot();
            } else {
                fileNamePattern = File.separator + "root-%d{yyyy-MM-dd}.%i.log";
                configuration.setFileNamePatternRoot(fileNamePattern);
            }
        }

        rollingPolicy.setFileNamePattern(fileNamePattern);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.start();

        rollingFileAppender.setRollingPolicy(rollingPolicy);
        rollingFileAppender.start();
        return rollingFileAppender;
    }

    private LayoutWrappingEncoder<ILoggingEvent> createEncoder(HyggeLogbackConfiguration configuration, boolean actualHyggeScope, boolean actualEnableColorful, OutputModeEnum actualOutputMode, LoggerContext loggerContext) {
        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(loggerContext);

        PatternLayout layout = new PatternLayout();
        layout.setContext(loggerContext);

        HyggeLogbackPatterHelper hyggeLogbackPatterHelper = new HyggeLogbackPatterHelper();

        String finalPatter;

        if (actualHyggeScope) {
            if (StringUtils.hasText(configuration.getHyggePattern())) {
                // 用户主动指定的 Pattern 优先级更高
                finalPatter = configuration.getHyggePattern();
            } else {
                // 默认值
                finalPatter = hyggeLogbackPatterHelper.createPatter(configuration, true, actualEnableColorful, actualOutputMode);
            }
        } else {
            if (StringUtils.hasText(configuration.getRootPattern())) {
                // 用户主动指定的 Pattern 优先级更高
                finalPatter = configuration.getRootPattern();
            } else {
                // 默认值
                finalPatter = hyggeLogbackPatterHelper.createPatter(configuration, false, actualEnableColorful, actualOutputMode);
            }
        }

        // %pid 占位符转化为实际 pid
        finalPatter = hyggeLogbackPatterHelper.pidConvert(finalPatter);

        layout.setPattern(finalPatter);

        Map<String, String> defaultConverterMap = layout.getDefaultConverterMap();

        if (OutputModeEnum.FILE.equals(configuration.getOutputMode()) || OutputModeEnum.CONSOLE_AND_FILE.equals(configuration.getOutputMode())) {
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
