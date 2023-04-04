/*
 * Copyright 2022-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hygge.web.config;

import hygge.commons.spring.HyggeSpringContext;
import hygge.commons.spring.config.configuration.definition.HyggeAutoConfiguration;
import hygge.web.util.log.ControllerLogAdvisor;
import hygge.web.util.log.inner.ControllerLogHandlerCache;
import hygge.web.util.log.ControllerLogInterceptor;
import hygge.web.util.log.ControllerLogPointCut;
import hygge.web.util.log.configuration.ControllerLogConfiguration;
import hygge.web.util.log.definition.ControllerLogHandlerFactory;
import hygge.web.util.log.impl.DefaultControllerLogHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@EnableConfigurationProperties(value = ControllerLogConfiguration.class)
@ConditionalOnProperty(value = "hygge.web-toolkit.controller.log.autoRegister", havingValue = "true", matchIfMissing = true)
public class ControllerLogAutoConfiguration implements HyggeAutoConfiguration, BeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(ControllerLogAutoConfiguration.class);

    @Bean("defaultControllerLogHandlerCache")
    @ConditionalOnMissingBean(value = ControllerLogHandlerCache.class)
    public ControllerLogHandlerCache defaultControllerLogHandlerCache() {
        return new ControllerLogHandlerCache();
    }

    @Bean("defaultControllerLogHandlerFactory")
    @ConditionalOnMissingBean(value = ControllerLogHandlerFactory.class)
    public ControllerLogHandlerFactory defaultControllerLogHandlerFactory() {
        return new DefaultControllerLogHandlerFactory();
    }

    @Bean("defaultControllerLogPointCut")
    @ConditionalOnMissingBean(value = ControllerLogPointCut.class)
    public ControllerLogPointCut defaultControllerLogPointCut(ControllerLogHandlerCache controllerLogHandlerCache, ControllerLogHandlerFactory controllerLogHandlerFactory) {
        return new ControllerLogPointCut(controllerLogHandlerCache, controllerLogHandlerFactory);
    }

    @Bean("defaultControllerLogInterceptor")
    @ConditionalOnMissingBean(value = ControllerLogInterceptor.class)
    public ControllerLogInterceptor defaultControllerLogInterceptor(ControllerLogHandlerCache controllerLogHandlerCache) {
        return new ControllerLogInterceptor(controllerLogHandlerCache);
    }

    @Bean("defaultControllerLogAdvisor")
    @ConditionalOnMissingBean(value = ControllerLogAdvisor.class)
    public ControllerLogAdvisor controllerLogAdvisor(ControllerLogPointCut pointCut, ControllerLogInterceptor interceptor) {
        int order = HyggeSpringContext.getConfigurableEnvironment().getProperty("hygge.web-toolkit.controller.log.aspectOrder",
                Integer.class,
                ControllerLogConfiguration.DEFAULT_ASPECT_ORDER);

        log.info("ControllerLog start to init({}).", order);
        return new ControllerLogAdvisor(order, interceptor, pointCut);
    }
}
