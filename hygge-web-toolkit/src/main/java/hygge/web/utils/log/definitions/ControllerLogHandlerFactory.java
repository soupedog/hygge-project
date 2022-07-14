package hygge.web.utils.log.definitions;

import hygge.commons.templates.core.annotation.HyggeExpressionInfo;
import hygge.web.utils.log.bo.ControllerLogType;

import java.util.Collection;

/**
 * ControllerLogHandler 工厂
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public interface ControllerLogHandlerFactory {
    /**
     * 创建一个 BaseControllerLogHandler 实例
     *
     * @param type                     拦截请求的类型
     * @param path                     拦截请求的 path
     * @param inputParamNames          入参名称列表
     * @param ignoreParamNames         需忽略的入参名称列表
     * @param inputParamGetExpressions 用于记录日志的入参获取表达式集合
     * @param outputParamExpressions   用于记录日志的出参获取表达式集合
     */
    BaseControllerLogHandler createHandler(ControllerLogType type, String path, String[] inputParamNames, Collection<String> ignoreParamNames, Collection<HyggeExpressionInfo> inputParamGetExpressions, Collection<HyggeExpressionInfo> outputParamExpressions);
}
