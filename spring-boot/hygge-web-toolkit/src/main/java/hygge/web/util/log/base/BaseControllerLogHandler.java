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

package hygge.web.util.log.base;

import hygge.commons.annotation.HyggeExpressionForInputFunction;
import hygge.commons.annotation.HyggeExpressionForOutputFunction;
import hygge.util.template.HyggeJsonUtilContainer;
import hygge.web.util.log.ControllerLogContext;
import hygge.web.util.log.annotation.ControllerLog;
import hygge.web.util.log.bo.ControllerLogInfo;
import hygge.web.util.log.enums.ControllerLogType;
import hygge.web.util.log.inner.ExpressionCache;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller 层自动日志执行器
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public abstract class BaseControllerLogHandler extends HyggeJsonUtilContainer {
    protected static final Logger log = LoggerFactory.getLogger(BaseControllerLogHandler.class);
    /**
     * 因 Spring 6.x 已移除 {@link LocalVariableTableParameterNameDiscoverer} 而切换为 {@link StandardReflectionParameterNameDiscoverer}
     * <br/>
     * 注意：应用必须携带编译参数 <code>-parameters</code> 才可正常工作
     */
    protected static final ParameterNameDiscoverer parameterNameDiscoverer = new StandardReflectionParameterNameDiscoverer();
    protected static final SpelExpressionParser spelExpressionParser = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, BaseControllerLogHandler.class.getClassLoader()));

    protected ControllerLogType type;
    protected String path;
    /**
     * 日志是否进行记录，该值为 {@link Boolean#FALSE} 也仍会执行  {@link BaseControllerLogHandler#hook(ControllerLogContext, MethodInvocation)}
     */
    protected boolean logRecordEnable = true;
    /**
     * 是否需要记录入参(方便切换到仅做统计，不关心出入参的场景)
     */
    protected boolean inputParamEnable = true;
    /**
     * 是否需要记录入参(方便切换到仅做统计，不关心出入参的场景)
     */
    protected boolean outputParamEnable = true;
    protected String[] inputParamNames;
    protected Collection<String> ignoreParamNames;
    /**
     * key:{@link HyggeExpressionForInputFunction#rootObjectName()}
     * 入参不像出参至多只有一个，所以 ExpressionCache 之外还多一层 map
     */
    protected final HashMap<String, ExpressionCache> inputExpressionCacheMap;
    protected final ExpressionCache outputExpressionCache;

    protected BaseControllerLogHandler(Method method, ControllerLogType type, String path, ControllerLog configuration) {
        String[] inputParamNamesTemp = parameterNameDiscoverer.getParameterNames(method);
        Collection<String> ignoreParamNamesTemp;
        Collection<HyggeExpressionForInputFunction> inputParamGetExpressions;
        Collection<HyggeExpressionForOutputFunction> outputParamExpressions;
        if (configuration != null) {
            ignoreParamNamesTemp = new ArrayList<>(Arrays.asList(configuration.ignoreParamNames()));
            inputParamGetExpressions = new ArrayList<>(Arrays.asList(configuration.inputParamGetExpressions()));
            outputParamExpressions = new ArrayList<>(Arrays.asList(configuration.outputParamExpressions()));
            this.inputParamEnable = configuration.inputParamEnable();
            this.outputParamEnable = configuration.outputParamEnable();
            this.logRecordEnable = configuration.logRecordEnable();
        } else {
            ignoreParamNamesTemp = new ArrayList<>(0);
            inputParamGetExpressions = new ArrayList<>(0);
            outputParamExpressions = new ArrayList<>(0);
        }

        this.type = type;
        this.path = path;
        this.inputParamNames = inputParamNamesTemp == null ? new String[0] : inputParamNamesTemp;
        this.ignoreParamNames = ignoreParamNamesTemp;
        this.inputExpressionCacheMap = new HashMap<>(inputParamGetExpressions.size(), 1F);
        this.outputExpressionCache = new ExpressionCache();

        // 初始化 inputExpressionCacheMap
        for (HyggeExpressionForInputFunction item : inputParamGetExpressions) {
            if (!item.enable()) {
                continue;
            }

            ExpressionCache currentExpressionCache = inputExpressionCacheMap.get(item.rootObjectName());

            if (currentExpressionCache == null) {
                currentExpressionCache = new ExpressionCache();
                inputExpressionCacheMap.put(item.rootObjectName(), currentExpressionCache);
            }

            Expression expression = spelExpressionParser.parseExpression(item.value());
            currentExpressionCache.saveValue(item.name(), expression);
        }

        // 初始化 outputExpressionCache
        for (HyggeExpressionForOutputFunction item : outputParamExpressions) {
            if (!item.enable()) {
                continue;
            }
            Expression expression = spelExpressionParser.parseExpression(item.value());
            outputExpressionCache.saveValue(item.name(), expression);
        }
    }

    /**
     * {@link BaseControllerLogHandler#defaultProcess(ControllerLogContext, MethodInvocation)} 、{@link BaseControllerLogHandler#processForNoneType(ControllerLogContext, MethodInvocation)} 执行完毕后执行的钩子函数
     * <p>
     * 这是是个统计监控需求很好的扩展点
     */
    protected abstract void hook(ControllerLogContext context, MethodInvocation methodInvocation);

    /**
     * logRecordEnable 为 {@link Boolean#TRUE} 时需要执行的自动日志生成流程
     */
    protected abstract Object defaultProcess(ControllerLogContext context, MethodInvocation methodInvocation) throws Throwable;

    /**
     * logRecordEnable 为 {@link Boolean#FALSE} 时需要执行的流程
     */
    protected abstract Object processForNoneType(ControllerLogContext context, MethodInvocation methodInvocation) throws Throwable;

    /**
     * 为被拦截的方法执行自动日志逻辑
     *
     * @param methodInvocation 被拦截的方法
     * @return 被拦截方法执行后的最终返回值
     */
    public Object executeHandler(MethodInvocation methodInvocation) throws Throwable {
        long startTs = System.currentTimeMillis();
        ControllerLogContext context = new ControllerLogContext(startTs);

        if (logRecordEnable) {
            try {
                return defaultProcess(context, methodInvocation);
            } catch (Exception e) {
                ControllerLogInfo logInfo = context.getLogInfo();
                logInfo.setErrorMessage(e.getMessage());
                throw e;
            } finally {
                ControllerLogInfo logInfo = context.getLogInfo();
                logInfo.setCost(System.currentTimeMillis() - startTs);

                hook(context, methodInvocation);

                String logInfoStringValue = getLogString(context);
                if (logInfo.getErrorMessage() != null) {
                    log.warn(logInfoStringValue);
                } else {
                    log.info(logInfoStringValue);
                }
            }
        } else {
            try {
                return processForNoneType(context, methodInvocation);
            } finally {
                hook(context, methodInvocation);
            }
        }
    }

    protected Object getInputParam(Object[] inputParameterValues) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        for (int i = 0; i < inputParameterValues.length; i++) {
            String inputParameterName = inputParamNames[i];
            if (ignoreParamNames.contains(inputParameterName)) {
                // 如果该属性指定了忽略
                continue;
            }

            Object inputParameter = inputParameterValues[i];

            if (inputExpressionCacheMap.isEmpty()) {
                if (inputParameter != null) {
                    result.put(inputParameterName, inputParameter);
                }
            } else {
                ExpressionCache expressionCache = inputExpressionCacheMap.get(inputParameterName);
                if (expressionCache != null) {
                    initResultByExpressionCache(inputParameter, result, expressionCache);
                }
            }
        }
        return result.isEmpty() ? null : result;
    }

    protected Object getOutputParam(Object responseEntityForLog) {
        if (outputExpressionCache.getContainer().isEmpty()) {
            return responseEntityForLog;
        }

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        initResultByExpressionCache(responseEntityForLog, result, outputExpressionCache);
        return result.isEmpty() ? null : result;
    }

    private void initResultByExpressionCache(Object rootObject, LinkedHashMap<String, Object> result, ExpressionCache expressionCache) {
        ArrayList<String> errorInfo = null;

        for (Map.Entry<String, Expression> entry : expressionCache.getContainer().entrySet()) {
            String parameterName = entry.getKey();
            Expression expression = entry.getValue();

            Object value = null;
            try {
                value = expression.getValue(rootObject);
            } catch (Exception e) {
                // 记日志发生的异常不应打断业务请求
                if (errorInfo == null) {
                    errorInfo = new ArrayList<>();
                }
                errorInfo.add(parameterName);
            }

            if (value == null) {
                continue;
            }
            result.put(parameterName, value);
        }

        if (errorInfo != null) {
            result.put("errorParameters", errorInfo);
        }
    }

    private String getLogString(ControllerLogContext context) {
        ControllerLogInfo controllerLogInfo = context.getLogInfo();
        String logInfoStringValue;
        try {
            logInfoStringValue = "ControllerLog:" + jsonHelper.formatAsString(controllerLogInfo);
        } catch (Exception e) {
            String message = "Can't serialize correctly.";
            log.error(message, e);
            controllerLogInfo.setInput(message);
            controllerLogInfo.setOutput(message);
            controllerLogInfo.setErrorMessage("An exception was triggered:" + e.getMessage());
            controllerLogInfo.setCost(System.currentTimeMillis() - context.getStartTs());
            logInfoStringValue = "ControllerLog:" + jsonHelper.formatAsString(controllerLogInfo);
        }
        return logInfoStringValue;
    }

    public Map<String, ExpressionCache> getInputExpressionCacheMap() {
        return inputExpressionCacheMap;
    }

    public ExpressionCache getOutputExpressionCache() {
        return outputExpressionCache;
    }
}
