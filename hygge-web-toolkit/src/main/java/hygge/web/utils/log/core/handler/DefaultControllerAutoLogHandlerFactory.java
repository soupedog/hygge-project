package hygge.web.utils.log.core.handler;

import hygge.commons.templates.core.annotation.HyggeExpressionInfo;
import hygge.web.utils.log.bo.ControllerAutoLogType;
import hygge.web.utils.log.core.ControllerAutoLogContext;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Collection;

/**
 * 默认的 ControllerAutoLogHandler 工厂实现
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public class DefaultControllerAutoLogHandlerFactory implements ControllerAutoLogHandlerFactory, BeanPostProcessor {
    @Override
    public BaseControllerAutoLogHandler createHandler(ControllerAutoLogType type, String path, String[] inputParamNames, Collection<String> ignoreParamNames, Collection<HyggeExpressionInfo> inputParamGetExpressions, Collection<HyggeExpressionInfo> outputParamExpressions) {
        return new BaseControllerAutoLogHandler(type, path, inputParamNames, ignoreParamNames, inputParamGetExpressions, outputParamExpressions) {
            @Override
            protected void hook(ControllerAutoLogContext context, MethodInvocation methodInvocation) {
                // 默认什么也不做
            }
        };
    }
}
