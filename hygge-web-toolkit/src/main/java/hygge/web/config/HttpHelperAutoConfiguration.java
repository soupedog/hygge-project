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
@ConditionalOnProperty(value = "hygge.utils.httpHelper.default.autoRegister", havingValue = "true", matchIfMissing = true)
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
    public HttpHelper defaultHttpHelper(HttpHelperRestTemplateFactory httpHelperRestTemplateFactory, HttpHelperLogger httpHelperLogger, HttpHelperResponseEntityReader httpHelperResponseEntityReader) {
        log.info("DefaultHttpHelper start to init.");
        return new DefaultHttpHelper(httpHelperRestTemplateFactory, httpHelperLogger, httpHelperResponseEntityReader);
    }
}
