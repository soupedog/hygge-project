package hygge.web.util.log;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Controller 层自动日志的 Advisor
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public class ControllerLogAdvisor extends AbstractBeanFactoryPointcutAdvisor implements BeanPostProcessor {
    /**
     * i just copy from {@link AbstractBeanFactoryPointcutAdvisor#advice}
     */
    private transient volatile Pointcut pointcut;

    public ControllerLogAdvisor(int order, MethodInterceptor methodInterceptor, Pointcut pointcut) {
        setOrder(order);
        setAdvice(methodInterceptor);
        this.pointcut = pointcut;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }
}
