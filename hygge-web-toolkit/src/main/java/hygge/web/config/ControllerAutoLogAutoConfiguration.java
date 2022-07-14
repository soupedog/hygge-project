package hygge.web.config;

import hygge.commons.spring.config.HyggeAutoConfiguration;
import hygge.web.utils.log.configuration.ControllerAutoLogConfiguration;
import hygge.web.utils.log.core.ControllerAutoLogAdvisor;
import hygge.web.utils.log.core.ControllerAutoLogHandlerCache;
import hygge.web.utils.log.core.ControllerAutoLogInterceptor;
import hygge.web.utils.log.core.ControllerAutoLogPointCut;
import hygge.web.utils.log.core.handler.ControllerAutoLogHandlerFactory;
import hygge.web.utils.log.core.handler.DefaultControllerAutoLogHandlerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Controller 层日志自动注册器
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties(value = ControllerAutoLogConfiguration.class)
@ConditionalOnProperty(value = "hygge.web-toolkit.controller.log.autoRegister", havingValue = "true", matchIfMissing = true)
public class ControllerAutoLogAutoConfiguration implements HyggeAutoConfiguration, BeanPostProcessor {
    @Bean("defaultControllerAutoLogHandlerCache")
    @ConditionalOnMissingBean(value = ControllerAutoLogHandlerCache.class)
    public ControllerAutoLogHandlerCache defaultControllerAutoLogHandlerCache() {
        return new ControllerAutoLogHandlerCache();
    }

    @Bean("defaultControllerAutoLogHandlerFactory")
    @ConditionalOnMissingBean(value = ControllerAutoLogHandlerFactory.class)
    public ControllerAutoLogHandlerFactory defaultControllerAutoLogHandlerFactory() {
        return new DefaultControllerAutoLogHandlerFactory();
    }

    @Bean("defaultControllerAutoLogPointCut")
    @ConditionalOnMissingBean(value = ControllerAutoLogPointCut.class)
    public ControllerAutoLogPointCut defaultControllerAutoLogPointCut(ControllerAutoLogHandlerCache controllerAutoLogInfoKeeper, ControllerAutoLogHandlerFactory controllerAutoLogHandlerFactory) {
        return new ControllerAutoLogPointCut(controllerAutoLogInfoKeeper, controllerAutoLogHandlerFactory);
    }

    @Bean("defaultControllerAutoLogInterceptor")
    @ConditionalOnMissingBean(value = ControllerAutoLogInterceptor.class)
    public ControllerAutoLogInterceptor defaultControllerAutoLogInterceptor(ControllerAutoLogHandlerCache controllerAutoLogInfoKeeper) {
        return new ControllerAutoLogInterceptor(controllerAutoLogInfoKeeper);
    }

    @Bean("defaultControllerAutoLogAdvisor")
    @ConditionalOnMissingBean(value = ControllerAutoLogAdvisor.class)
    public ControllerAutoLogAdvisor controllerAutoLogAdvisor(ControllerAutoLogPointCut pointCut, ControllerAutoLogInterceptor interceptor) {
        return new ControllerAutoLogAdvisor(interceptor, pointCut);
    }
}
