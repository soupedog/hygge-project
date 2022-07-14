package hygge.web.utils.log.core.handler;

import hygge.commons.templates.core.annotation.HyggeExpressionInfo;
import hygge.web.utils.log.bo.ControllerAutoLogType;

import java.util.Collection;

/**
 * ControllerAutoLogHandler 工厂
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public interface ControllerAutoLogHandlerFactory {
    /**
     * 创建一个 BaseControllerAutoLogHandler 实例
     *
     * @param type                     拦截请求的类型
     * @param path                     拦截请求的 path
     * @param inputParamNames          入参名称列表
     * @param ignoreParamNames         需忽略的入参名称列表
     * @param inputParamGetExpressions 用于记录日志的入参获取表达式集合
     * @param outputParamExpressions   用于记录日志的出参获取表达式集合
     */
    BaseControllerAutoLogHandler createHandler(ControllerAutoLogType type, String path, String[] inputParamNames, Collection<String> ignoreParamNames, Collection<HyggeExpressionInfo> inputParamGetExpressions, Collection<HyggeExpressionInfo> outputParamExpressions);
}
