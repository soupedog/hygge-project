/*
 * Copyright 2022-2023 the original author or authors.
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

import hygge.commons.spring.HyggeSpringContext;
import hygge.commons.spring.listener.HyggeSpringContextInitListener;
import hygge.logging.enums.OutputModeEnum;
import hygge.logging.log4j.configuration.HyggeLg4jConfiguration;
import hygge.logging.util.HyggeLog4JPatterHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.CronTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 配置 Logger 对象
 *
 * @author Xavier
 * @date 2022/7/16
 * @since 1.0
 */
public class HyggeLogLog4jLoaderListener implements Ordered, ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    public static final String LOGGER_PREFIX = "Hygge";
    private static final Logger log = LogManager.getLogger(HyggeLogLog4jLoaderListener.class);
    /**
     * 此对象和 HyggeLogLog4jLoaderListener 其实没关系，只是希望是在日志配置完毕后再输出 HyggeContext 内容，故放在此处
     */
    private static Logger logForHyggeSpringContext = LogManager.getLogger(HyggeSpringContextInitListener.class);

    @Override
    public int getOrder() {
        // 设置优先级比日志系统默认值低一点
        return LoggingApplicationListener.DEFAULT_ORDER + 2;
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        HyggeLg4jConfiguration configuration = new HyggeLg4jConfiguration();
        if (configuration.isEnable()) {
            loadLog4j(configuration);
            log.info("HyggeLoggerLog4j init success");
        }

        String logInfo = "HyggeContext init success:" + HyggeSpringContext.toJsonVale();
        logForHyggeSpringContext.info(logInfo);
    }

    protected void loadLog4j(HyggeLg4jConfiguration hyggeLg4jConfiguration) {
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = loggerContext.getConfiguration();

        if (hyggeLg4jConfiguration.isEnableRootOverride()) {
            List<Appender> rootAppenderList = createAppenderList(false, hyggeLg4jConfiguration, configuration);

            // 剔除默认的 appender
            configuration.getRootLogger().removeAppender("Console");

            for (Appender appender : rootAppenderList) {
                appender.start();
                configuration.getRootLogger().addAppender(appender, configuration.getRootLogger().getLevel(), configuration.getFilter());
                configuration.addAppender(appender);
            }

            restLogger(loggerContext, configuration);
        }

        Map<String, Boolean> hyggeScopePathsMap = hyggeLg4jConfiguration.getHyggeScopePaths();
        // 不存在 hygge 目录定义时直接跳过初始化
        if (hyggeScopePathsMap.isEmpty()) {
            return;
        }

        AppenderRef appenderRef = AppenderRef.createAppenderRef(LOGGER_PREFIX + "AppenderRef", Level.INFO, null);
        AppenderRef[] appenderRefArray = new AppenderRef[]{appenderRef};

        LoggerConfig hyggeLoggerConfiguration = LoggerConfig.newBuilder()
                .withLoggerName(LOGGER_PREFIX + "LoggerConfig")
                // 以 Root 配置为默认值
                .withConfig(configuration)
                // additivity：false 子 Logger 的  appender 不传递给 root logger 防止日志输出两次
                .withAdditivity(false)
                .withIncludeLocation("true")
                .withRefs(appenderRefArray)
                .build();

        List<Appender> hyggeAppenderList = createAppenderList(true, hyggeLg4jConfiguration, configuration);

        for (Appender appender : hyggeAppenderList) {
            appender.start();
            hyggeLoggerConfiguration.addAppender(appender, Level.INFO, null);
        }

        // 初始化 hyggeLogger
        for (Map.Entry<String, Boolean> entry : hyggeScopePathsMap.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                configuration.addLogger(entry.getKey(), hyggeLoggerConfiguration);
            }
        }

        // 刷新日志配置
        restLogger(loggerContext, configuration);
    }

    private void restLogger(LoggerContext loggerContext, Configuration configuration) {
        loggerContext.updateLoggers(configuration);
    }

    private List<Appender> createAppenderList(boolean actualHyggeScope, HyggeLg4jConfiguration hyggeLg4jConfiguration, Configuration configuration) {
        List<Appender> result = new ArrayList<>();

        OutputModeEnum outputMode = hyggeLg4jConfiguration.getOutputMode();

        if (OutputModeEnum.CONSOLE.equals(outputMode)
                || OutputModeEnum.CONSOLE_AND_FILE.equals(outputMode)) {
            Layout<?> consoleLayout = createLayout(configuration, hyggeLg4jConfiguration, actualHyggeScope, hyggeLg4jConfiguration.getEnableColorfulConsole(), OutputModeEnum.CONSOLE);

            ConsoleAppender consoleAppender = ConsoleAppender.newBuilder()
                    .setName("HyggeConsoleAppender")
                    .setLayout(consoleLayout)
                    .build();

            result.add(consoleAppender);
        }

        if (OutputModeEnum.FILE.equals(outputMode)
                || OutputModeEnum.CONSOLE_AND_FILE.equals(outputMode)) {

            Layout<?> fileLayout = createLayout(configuration, hyggeLg4jConfiguration, actualHyggeScope, hyggeLg4jConfiguration.getEnableColorfulFile(), OutputModeEnum.FILE);

            SizeBasedTriggeringPolicy sizeBasedTriggeringPolicy = SizeBasedTriggeringPolicy.createPolicy(hyggeLg4jConfiguration.getFileMaxSize());
            // false 服务启动时，不立即做文件迁移评估，只有 Cron 表达式被触发的时刻才做文件迁移评估
            CronTriggeringPolicy cronTriggeringPolicy = CronTriggeringPolicy.createPolicy(configuration, Boolean.FALSE.toString(), hyggeLg4jConfiguration.getCronTrigger());
            // 定时触发、文件大小触发
            CompositeTriggeringPolicy compositeTriggeringPolicy = CompositeTriggeringPolicy.createPolicy(sizeBasedTriggeringPolicy, cronTriggeringPolicy);
            String fileTypeName = actualHyggeScope ? "hygge" : "root";
            String filePrefix = hyggeLg4jConfiguration.getFilePath() + File.separator + hyggeLg4jConfiguration.getProjectName() + "_" + hyggeLg4jConfiguration.getAppName() + "_" + fileTypeName;

            RollingFileAppender fileAppender = RollingFileAppender.newBuilder()
                    .withFileName(filePrefix + ".log")
                    .withFilePattern(filePrefix + "%d{yyyy-MM-dd}_%i.log")
                    .setName("HyggeRollingFileAppender")
                    .withPolicy(compositeTriggeringPolicy)
                    .setLayout(fileLayout)
                    .withLocking(false)
                    .build();

            result.add(fileAppender);
        }

        return result;
    }

    private PatternLayout createLayout(Configuration configuration, HyggeLg4jConfiguration hyggeLg4jConfiguration, boolean actualHyggeScope, boolean actualEnableColorful, OutputModeEnum actualOutputMode) {
        HyggeLog4JPatterHelper patterHelper = new HyggeLog4JPatterHelper();
        String finalPatter = patterHelper.createPatter(hyggeLg4jConfiguration, actualHyggeScope, actualEnableColorful, actualOutputMode);

        return PatternLayout.newBuilder()
                .withCharset(UTF_8)
                .withPattern(finalPatter)
                .withPatternSelector(null)
                .withConfiguration(configuration)
                .withRegexReplacement(null)
                .withAlwaysWriteExceptions(true)
                .withNoConsoleNoAnsi(false)
                .withHeader(null)
                .withFooter(null)
                .build();
    }
}
