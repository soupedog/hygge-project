package hygge.web.utils.log.core;

import hygge.web.utils.log.core.handler.BaseControllerAutoLogHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Controller 自动日志拦截器
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public class ControllerAutoLogInterceptor implements MethodInterceptor {
    protected ControllerAutoLogHandlerCache controllerAutoLogHandlerCache;

    public ControllerAutoLogInterceptor(ControllerAutoLogHandlerCache controllerAutoLogHandlerCache) {
        this.controllerAutoLogHandlerCache = controllerAutoLogHandlerCache;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        BaseControllerAutoLogHandler baseControllerAutoLogHandler = controllerAutoLogHandlerCache.getValue(methodInvocation.getMethod());
        return baseControllerAutoLogHandler.executeHandler(methodInvocation);
    }
}
