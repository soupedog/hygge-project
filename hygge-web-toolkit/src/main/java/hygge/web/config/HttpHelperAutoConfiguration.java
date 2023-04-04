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

import hygge.commons.spring.config.configuration.definition.HyggeAutoConfiguration;
import hygge.web.util.http.configuration.HttpHelperConfiguration;
import hygge.web.util.http.definition.HttpHelper;
import hygge.web.util.http.definition.HttpHelperLogger;
import hygge.web.util.http.definition.HttpHelperResponseEntityReader;
import hygge.web.util.http.definition.HttpHelperRestTemplateFactory;
import hygge.web.util.http.impl.DefaultHttpHelper;
import hygge.web.util.http.impl.DefaultHttpHelperLogger;
import hygge.web.util.http.impl.DefaultHttpHelperResponseEntityReader;
import hygge.web.util.http.impl.DefaultHttpHelperRestTemplateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HttpHelper 自动注册器
 *
 * @author Xavier
 * @date 2022/7/13
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties(value = HttpHelperConfiguration.class)
@ConditionalOnProperty(value = "hygge.util.httpHelper.default.autoRegister", havingValue = "true", matchIfMissing = true)
public class HttpHelperAutoConfiguration implements HyggeAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(HttpHelperAutoConfiguration.class);

    @Bean("defaultHttpHelperRestTemplateFactory")
    @ConditionalOnMissingBean(value = HttpHelperRestTemplateFactory.class)
    public HttpHelperRestTemplateFactory defaultHttpHelperRestTemplateFactory(HttpHelperConfiguration httpHelperConfiguration) {
        return new DefaultHttpHelperRestTemplateFactory(httpHelperConfiguration);
    }

    @Bean("defaultHttpHelperResponseEntityReader")
    @ConditionalOnMissingBean(value = HttpHelperResponseEntityReader.class)
    public HttpHelperResponseEntityReader defaultHttpHelperResponseEntityReader() {
        return new DefaultHttpHelperResponseEntityReader();
    }

    @Bean("defaultHttpHelperLogger")
    @ConditionalOnMissingBean(value = HttpHelperLogger.class)
    public HttpHelperLogger defaultHttpHelperLogger() {
        return new DefaultHttpHelperLogger();
    }

    @Bean("defaultHttpHelper")
    @ConditionalOnMissingBean(value = HttpHelper.class)
    public DefaultHttpHelper defaultHttpHelper(HttpHelperRestTemplateFactory httpHelperRestTemplateFactory, HttpHelperLogger httpHelperLogger, HttpHelperResponseEntityReader httpHelperResponseEntityReader) {
        log.info("DefaultHttpHelper start to init.");
        return new DefaultHttpHelper(httpHelperRestTemplateFactory, httpHelperLogger, httpHelperResponseEntityReader);
    }
}
