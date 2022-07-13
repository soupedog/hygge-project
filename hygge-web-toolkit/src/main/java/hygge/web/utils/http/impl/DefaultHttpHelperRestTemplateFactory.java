package hygge.web.utils.http.impl;

import hygge.commons.exceptions.UtilRuntimeException;
import hygge.commons.templates.container.base.AbstractHyggeKeeper;
import hygge.utils.UtilsCreator;
import hygge.utils.definitions.ParameterHelper;
import hygge.web.utils.http.configuration.HttpHelperConfiguration;
import hygge.web.utils.http.configuration.HttpHelperRequestConfiguration;
import hygge.web.utils.http.definitions.HttpHelperRestTemplateFactory;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 网络请求工具 RestTemplate 构造器 默认实现
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public class DefaultHttpHelperRestTemplateFactory extends AbstractHyggeKeeper<HttpHelperRequestConfiguration, RestTemplate> implements HttpHelperRestTemplateFactory {
    protected static ParameterHelper parameterHelper = UtilsCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    protected HttpHelperConfiguration httpHelperConfiguration;
    protected HttpHelperRequestConfiguration defaultRequestConfiguration;
    private final HttpClientBuilder httpClientBuilder;
    private final HttpClientBuilder ignoreSSLHttpClientBuilder;
    protected static final StringHttpMessageConverter STRING_HTTP_MESSAGE_CONVERTER_UTF8 = new StringHttpMessageConverter(StandardCharsets.UTF_8);
    protected static final ResponseErrorHandler DEFAULT_RESPONSE_ERROR_HANDLER = new ResponseErrorHandler() {
        @Override
        public boolean hasError(ClientHttpResponse response) {
            return true;
        }

        @Override
        public void handleError(ClientHttpResponse response) {
            // 为了请求异常也不中断业务流程，不抛出异常
        }
    };

    public DefaultHttpHelperRestTemplateFactory(HttpHelperConfiguration httpHelperConfiguration) {
        this.httpHelperConfiguration = httpHelperConfiguration;

        this.defaultRequestConfiguration = new HttpHelperRequestConfiguration(
                httpHelperConfiguration.getLogType(),
                httpHelperConfiguration.isIgnoreSSL(),
                httpHelperConfiguration.getConnection().getConnectTimeOut().toMillis(),
                httpHelperConfiguration.getConnection().getReadTimeOut().toMillis()
        );

        this.httpClientBuilder = HttpClientBuilder.create();
        this.httpClientBuilder.setConnectionManager(getHttpClientConnectionManager(false));
        this.ignoreSSLHttpClientBuilder = HttpClientBuilder.create();
        this.ignoreSSLHttpClientBuilder.setConnectionManager(getHttpClientConnectionManager(true));
    }

    @Override
    public RestTemplate getInstance(HttpHelperRequestConfiguration config) {
        if (config == null) {
            config = defaultRequestConfiguration;
        }

        if (containsKey(config)) {
            return getValue(config);
        }
        return newInstance(config);
    }

    @Override
    public HttpHelperRequestConfiguration getDefaultConfiguration() {
        return defaultRequestConfiguration;
    }

    private synchronized RestTemplate newInstance(HttpHelperRequestConfiguration config) {
        if (containsKey(config)) {
            return getValue(config);
        }

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        CloseableHttpClient closeableHttpClient = config.ignoreSSL() ? ignoreSSLHttpClientBuilder.build()
                : httpClientBuilder.build();
        httpRequestFactory.setHttpClient(closeableHttpClient);

        httpRequestFactory.setConnectTimeout(parameterHelper.integerFormat("connectTimeOutMilliseconds", config.connectTimeOutMilliseconds()));
        httpRequestFactory.setReadTimeout(parameterHelper.integerFormat("readTimeOutMilliseconds", config.readTimeOutMilliseconds()));

        RestTemplate result = new RestTemplate(httpRequestFactory);
        toSupportUTF8(result);
        result.setErrorHandler(getResponseErrorHandler());

        saveValue(config, result);
        return result;
    }

    protected void toSupportUTF8(RestTemplate result) {
        List<HttpMessageConverter<?>> converters = result.getMessageConverters();
        int needReplaceIndex;
        boolean needRemove = false;
        for (needReplaceIndex = 0; needReplaceIndex < converters.size(); needReplaceIndex++) {
            if (converters.get(needReplaceIndex) instanceof StringHttpMessageConverter) {
                needRemove = true;
                break;
            }
        }
        if (needRemove) {
            converters.remove(needReplaceIndex);
            converters.add(needReplaceIndex, STRING_HTTP_MESSAGE_CONVERTER_UTF8);
            result.setMessageConverters(converters);
        }
    }

    private ResponseErrorHandler getResponseErrorHandler() {
        return DEFAULT_RESPONSE_ERROR_HANDLER;
    }

    private HttpClientConnectionManager getHttpClientConnectionManager(boolean ignoreSSL) {
        PoolingHttpClientConnectionManager poolingConnectionManager;

        if (ignoreSSL) {
            TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
            SSLContext sslContext;
            try {
                sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            } catch (Exception e) {
                throw new UtilRuntimeException("Fail to init DefaultHttpHelperRestTemplateFactory.", e);
            }
            SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", connectionSocketFactory)
                    .build();
            poolingConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        } else {
            poolingConnectionManager = new PoolingHttpClientConnectionManager();
        }

        poolingConnectionManager.setMaxTotal(httpHelperConfiguration.getConnection().getMaxTotal());
        poolingConnectionManager.setDefaultMaxPerRoute(httpHelperConfiguration.getConnection().getMaxPerRoute());
        return poolingConnectionManager;
    }
}
