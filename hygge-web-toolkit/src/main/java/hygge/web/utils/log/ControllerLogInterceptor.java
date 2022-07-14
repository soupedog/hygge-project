package hygge.web.utils.log;

import hygge.web.utils.log.definitions.BaseControllerLogHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Controller 自动日志拦截器
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public class ControllerLogInterceptor implements MethodInterceptor {
    protected ControllerLogHandlerCache controllerLogHandlerCache;

    public ControllerLogInterceptor(ControllerLogHandlerCache controllerLogHandlerCache) {
        this.controllerLogHandlerCache = controllerLogHandlerCache;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        BaseControllerLogHandler baseControllerLogHandler = controllerLogHandlerCache.getValue(methodInvocation.getMethod());
        return baseControllerLogHandler.executeHandler(methodInvocation);
    }
}
