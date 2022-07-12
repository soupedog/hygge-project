package hygge.web.config;

import hygge.commons.spring.config.HyggeAutoConfiguration;
import hygge.web.utils.http.configuration.HttpHelperConfiguration;
import hygge.web.utils.http.definitions.HttpHelper;
import hygge.web.utils.http.definitions.HttpHelperLogger;
import hygge.web.utils.http.definitions.HttpHelperResponseEntityReader;
import hygge.web.utils.http.definitions.HttpHelperRestTemplateFactory;
import hygge.web.utils.http.impl.DefaultHttpHelper;
import hygge.web.utils.http.impl.DefaultHttpHelperLogger;
import hygge.web.utils.http.impl.DefaultHttpHelperResponseEntityReader;
import hygge.web.utils.http.impl.DefaultHttpHelperRestTemplateFactory;
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
public class HttpHelperAutoConfiguration implements HyggeAutoConfiguration {
    @Bean("defaultHttpHelperRestTemplateFactory")
    @ConditionalOnProperty(value = "hygge.utils.httpHelper.default.autoRegister", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(value = HttpHelperRestTemplateFactory.class)
    public HttpHelperRestTemplateFactory defaultHttpHelperRestTemplateFactory(HttpHelperConfiguration httpHelperConfiguration) {
        return new DefaultHttpHelperRestTemplateFactory(httpHelperConfiguration);
    }

    @Bean("defaultHttpHelperResponseEntityReader")
    @ConditionalOnProperty(value = "hygge.utils.httpHelper.default.autoRegister", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(value = HttpHelperResponseEntityReader.class)
    public HttpHelperResponseEntityReader defaultHttpHelperResponseEntityReader() {
        return new DefaultHttpHelperResponseEntityReader();
    }

    @Bean("defaultHttpHelperLogger")
    @ConditionalOnProperty(value = "hygge.utils.httpHelper.default.autoRegister", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(value = HttpHelperLogger.class)
    public HttpHelperLogger defaultHttpHelperLogger() {
        return new DefaultHttpHelperLogger();
    }

    @Bean("defaultHttpHelper")
    @ConditionalOnProperty(value = "hygge.utils.httpHelper.default.autoRegister", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(value = HttpHelper.class)
    public HttpHelper httpHelper(HttpHelperRestTemplateFactory httpHelperRestTemplateFactory, HttpHelperLogger httpHelperLogger, HttpHelperResponseEntityReader httpHelperResponseEntityReader) {
        return new DefaultHttpHelper(httpHelperRestTemplateFactory, httpHelperLogger, httpHelperResponseEntityReader);
    }
}
