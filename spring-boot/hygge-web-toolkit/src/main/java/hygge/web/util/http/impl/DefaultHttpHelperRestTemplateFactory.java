/*
 * Copyright 2022-2024 the original author or authors.
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

package hygge.web.util.http.impl;

import hygge.commons.exception.UtilRuntimeException;
import hygge.util.UtilCreator;
import hygge.util.definition.ParameterHelper;
import hygge.web.util.http.configuration.HttpHelperConfiguration;
import hygge.web.util.http.configuration.HttpHelperRequestConfiguration;
import hygge.web.util.http.definition.HttpHelperRestTemplateFactory;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
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
public class DefaultHttpHelperRestTemplateFactory implements HttpHelperRestTemplateFactory {
    protected static ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    protected HttpHelperConfiguration httpHelperConfiguration;
    protected HttpHelperRequestConfiguration defaultRequestConfiguration;
    protected RestTemplateKeeper restTemplateKeeper;
    protected static final StringHttpMessageConverter STRING_HTTP_MESSAGE_CONVERTER_UTF8 = new StringHttpMessageConverter(StandardCharsets.UTF_8);
    protected static final ResponseErrorHandler DEFAULT_RESPONSE_ERROR_HANDLER = new ResponseErrorHandler() {
        @Override
        public boolean hasError(ClientHttpResponse response) {
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse response) {
            // hasError() 已经固定为 false 所以该方法永远不会被触发
        }
    };

    public DefaultHttpHelperRestTemplateFactory(HttpHelperConfiguration httpHelperConfiguration) {
        initConfiguration(httpHelperConfiguration);
        this.restTemplateKeeper = new RestTemplateKeeper();
    }

    public DefaultHttpHelperRestTemplateFactory(HttpHelperConfiguration httpHelperConfiguration, RestTemplateKeeper restTemplateKeeper) {
        initConfiguration(httpHelperConfiguration);
        this.restTemplateKeeper = restTemplateKeeper;
    }

    protected void initConfiguration(HttpHelperConfiguration httpHelperConfiguration) {
        this.httpHelperConfiguration = httpHelperConfiguration;
        this.defaultRequestConfiguration = new HttpHelperRequestConfiguration(
                httpHelperConfiguration.getLogType(),
                httpHelperConfiguration.isIgnoreSSL(),
                httpHelperConfiguration.getConnection().getConnectTimeOut().toMillis(),
                httpHelperConfiguration.getConnection().getReadTimeOut().toMillis()
        );
    }

    @Override
    public RestTemplate getInstance(HttpHelperRequestConfiguration config) {
        if (config == null) {
            config = defaultRequestConfiguration;
        }

        RestTemplate result = restTemplateKeeper.getValue(config);
        if (result == null) {
            result = newInstance(config);
        }
        return result;
    }

    @Override
    public HttpHelperRequestConfiguration getDefaultConfiguration() {
        return defaultRequestConfiguration;
    }

    private synchronized RestTemplate newInstance(HttpHelperRequestConfiguration config) {
        RestTemplate result = restTemplateKeeper.getValue(config);
        if (result != null) {
            return result;
        }

        HttpClientBuilder httpClientBuilder = HttpClientBuilder
                .create()
                .setConnectionManager(getHttpClientConnectionManager(config));

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setHttpClient(httpClientBuilder.build());
        httpRequestFactory.setConnectTimeout(parameterHelper.integerFormat("connectTimeOutMilliseconds", config.connectTimeOutMilliseconds()));

        result = new RestTemplate(httpRequestFactory);

        toSupportUTF8(result);
        result.setErrorHandler(DEFAULT_RESPONSE_ERROR_HANDLER);

        restTemplateKeeper.saveValue(config, result);
        return result;
    }

    private HttpClientConnectionManager getHttpClientConnectionManager(HttpHelperRequestConfiguration config) {
        PoolingHttpClientConnectionManager poolingConnectionManager;

        if (config.ignoreSSL()) {
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

        SocketConfig.Builder builder = SocketConfig.custom();

        SocketConfig socketConfig = builder.setSoTimeout(parameterHelper.integerFormat("readTimeOutMilliseconds", config.readTimeOutMilliseconds()))
                .build();

        poolingConnectionManager.setDefaultSocketConfig(socketConfig);

        poolingConnectionManager.setMaxTotal(httpHelperConfiguration.getConnection().getMaxTotal());
        poolingConnectionManager.setDefaultMaxPerRoute(httpHelperConfiguration.getConnection().getMaxPerRoute());
        return poolingConnectionManager;
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

    public RestTemplateKeeper getRestTemplateKeeper() {
        return restTemplateKeeper;
    }

    public void setRestTemplateKeeper(RestTemplateKeeper restTemplateKeeper) {
        this.restTemplateKeeper = restTemplateKeeper;
    }

    public void reloadHttpHelperConfiguration(HttpHelperConfiguration httpHelperConfiguration) {
        initConfiguration(httpHelperConfiguration);
    }
}
