package hygge.web.util.log;

import hygge.web.util.log.bo.ControllerLogHandlerCache;
import hygge.web.util.log.base.BaseControllerLogHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Controller 自动日志拦截器
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public class ControllerLogInterceptor implements MethodInterceptor, BeanPostProcessor {
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
