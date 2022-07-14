package hygge.web.utils.log.definitions;

import hygge.commons.templates.core.annotation.HyggeExpressionInfo;
import hygge.web.template.HyggeWebUtilContainer;
import hygge.web.utils.log.bo.ControllerLogType;
import hygge.web.utils.log.bo.ControllerLogInfo;
import hygge.web.utils.log.ControllerLogContext;
import hygge.web.utils.log.ExpressionCache;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
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
public abstract class BaseControllerLogHandler extends HyggeWebUtilContainer {
    protected static final Logger log = LoggerFactory.getLogger(BaseControllerLogHandler.class);
    protected static final SpelExpressionParser spelExpressionParser = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, BaseControllerLogHandler.class.getClassLoader()));

    protected ControllerLogType type;
    protected String path;
    protected String[] inputParamNames;
    protected Collection<String> ignoreParamNames;
    /**
     * key:{@link HyggeExpressionInfo#rootObjectName()}
     */
    protected final HashMap<String, ExpressionCache> inputExpressionCacheMap;
    protected final ExpressionCache outputExpressionCache;

    /**
     * 在打印日志前的最后一刻执行的钩子函数
     */
    protected abstract void hook(ControllerLogContext context, MethodInvocation methodInvocation);

    protected BaseControllerLogHandler(ControllerLogType type, String path, String[] inputParamNames, Collection<String> ignoreParamNames, Collection<HyggeExpressionInfo> inputParamGetExpressions, Collection<HyggeExpressionInfo> outputParamExpressions) {
        this.type = type;
        this.path = path;
        this.inputParamNames = inputParamNames == null ? new String[0] : inputParamNames;
        this.ignoreParamNames = ignoreParamNames == null ? new ArrayList<>(0) : ignoreParamNames;
        this.inputExpressionCacheMap = new HashMap<>(inputParamGetExpressions.size(), 1F);
        this.outputExpressionCache = new ExpressionCache();

        // 初始化 inputExpressionCacheMap
        for (HyggeExpressionInfo item : inputParamGetExpressions) {
            ExpressionCache currentExpressionCache;
            if (!inputExpressionCacheMap.containsKey(item.rootObjectName())) {
                currentExpressionCache = new ExpressionCache();
                inputExpressionCacheMap.put(item.rootObjectName(), currentExpressionCache);
            } else {
                currentExpressionCache = inputExpressionCacheMap.get(item.rootObjectName());
            }
            Expression expression = spelExpressionParser.parseExpression(item.value());
            currentExpressionCache.saveValue(item.name(), expression);
        }
        // 初始化 outputExpressionCache
        for (HyggeExpressionInfo item : outputParamExpressions) {
            Expression expression = spelExpressionParser.parseExpression(item.value());
            outputExpressionCache.saveValue(item.name(), expression);
        }
    }

    public boolean matches(ControllerLogType type) {
        return this.type.equals(type);
    }

    /**
     * 为被拦截的方法执行自动日志逻辑
     *
     * @param methodInvocation 被拦截的方法
     * @return 被拦截方法执行后的最终返回值
     */
    public Object executeHandler(MethodInvocation methodInvocation) throws Throwable {
        long startTs = System.currentTimeMillis();
        ControllerLogContext context = new ControllerLogContext(startTs);
        try {
            ControllerLogInfo controllerLogInfo = context.getLogInfo();
            controllerLogInfo.setType(type);
            controllerLogInfo.setPath(path);

            Object[] inputParameterValues = methodInvocation.getArguments();
            controllerLogInfo.setInput(getInputParam(inputParameterValues));

            Object responseEntity = methodInvocation.proceed();
            context.saveObject(ControllerLogContext.Key.RAW_RESPONSE, responseEntity);

            Object responseEntityForLog;

            if (responseEntity instanceof ResponseEntity) {
                responseEntityForLog = ((ResponseEntity<?>) responseEntity).getBody();
            } else {
                responseEntityForLog = responseEntity;
            }

            controllerLogInfo.setOutput(getOutputParam(responseEntityForLog));

            hook(context, methodInvocation);

            return responseEntity;
        } catch (Exception e) {
            ControllerLogInfo logInfo = context.getLogInfo();
            logInfo.setErrorMessage("An exception was triggered:" + e.getMessage());
            throw e;
        } finally {
            ControllerLogInfo logInfo = context.getLogInfo();
            logInfo.setCost(System.currentTimeMillis() - startTs);
            String logInfoStringValue = getLogString(context);
            log.info(logInfoStringValue);
        }
    }

    protected Object getInputParam(Object[] inputParameterValues) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        for (int i = 0; i < inputParameterValues.length; i++) {
            String rootObjectName = inputParamNames[i];
            if (ignoreParamNames.contains(rootObjectName)) {
                // 如果该属性指定了忽略
                continue;
            }

            Object rootObject = inputParameterValues[i];

            if (inputExpressionCacheMap.isEmpty()) {
                if (rootObject != null) {
                    result.put(rootObjectName, rootObject);
                }
            } else {
                ExpressionCache expressionCache = inputExpressionCacheMap.get(rootObjectName);
                if (expressionCache != null) {
                    initResultByExpressionCache(rootObject, result, expressionCache);
                }
            }
        }
        return result.isEmpty() ? null : result;
    }

    protected Object getOutputParam(Object responseEntityForLog) {
        if (inputExpressionCacheMap.isEmpty()) {
            return responseEntityForLog;
        }

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        initResultByExpressionCache(responseEntityForLog, result, outputExpressionCache);
        return result.isEmpty() ? null : result;
    }

    private void initResultByExpressionCache(Object rootObject, LinkedHashMap<String, Object> result, ExpressionCache expressionCache) {
        for (Map.Entry<String, Expression> entry : expressionCache.getContainer().entrySet()) {
            String parameterName = entry.getKey();
            Expression expression = entry.getValue();

            Object value = expression.getValue(rootObject);
            if (value == null) {
                continue;
            }
            result.put(parameterName, value);
        }
    }

    private String getLogString(ControllerLogContext context) {
        ControllerLogInfo controllerLogInfo = context.getLogInfo();
        String logInfoStringValue;
        try {
            logInfoStringValue = "ControllerLog:" + jsonHelper.formatAsString(controllerLogInfo);
        } catch (Exception e) {
            String message = "Cannot serialize correctly.";
            log.error(message, e);
            controllerLogInfo.setInput(message);
            controllerLogInfo.setOutput(message);
            controllerLogInfo.setErrorMessage("An exception was triggered:" + e.getMessage());
            controllerLogInfo.setCost(System.currentTimeMillis() - context.getStartTs());
            logInfoStringValue = "ControllerLog:" + jsonHelper.formatAsString(controllerLogInfo);
        }
        return logInfoStringValue;
    }

    public HashMap<String, ExpressionCache> getInputExpressionCacheMap() {
        return inputExpressionCacheMap;
    }

    public ExpressionCache getOutputExpressionCache() {
        return outputExpressionCache;
    }
}
