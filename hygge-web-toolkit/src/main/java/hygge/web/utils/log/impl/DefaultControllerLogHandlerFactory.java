package hygge.web.utils.log.impl;

import hygge.commons.annotation.HyggeExpressionInfo;
import hygge.web.utils.log.bo.ControllerLogType;
import hygge.web.utils.log.ControllerLogContext;
import hygge.web.utils.log.definitions.BaseControllerLogHandler;
import hygge.web.utils.log.definitions.ControllerLogHandlerFactory;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Collection;

/**
 * 默认的 ControllerLogHandler 工厂实现
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public class DefaultControllerLogHandlerFactory implements ControllerLogHandlerFactory, BeanPostProcessor {
    @Override
    public BaseControllerLogHandler createHandler(ControllerLogType type, String path, String[] inputParamNames, Collection<String> ignoreParamNames, Collection<HyggeExpressionInfo> inputParamGetExpressions, Collection<HyggeExpressionInfo> outputParamExpressions) {
        return new BaseControllerLogHandler(type, path, inputParamNames, ignoreParamNames, inputParamGetExpressions, outputParamExpressions) {
            @Override
            protected void hook(ControllerLogContext context, MethodInvocation methodInvocation) {
                // 默认什么也不做
            }
        };
    }
}
