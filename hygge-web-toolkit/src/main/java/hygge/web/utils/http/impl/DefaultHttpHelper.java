package hygge.web.utils.http.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import hygge.commons.exception.UtilRuntimeException;
import hygge.web.utils.http.bo.HttpResponse;
import hygge.web.utils.http.configuration.HttpHelperRequestConfiguration;
import hygge.web.utils.http.definitions.HttpHelper;
import hygge.web.utils.http.definitions.HttpHelperLogger;
import hygge.web.utils.http.definitions.HttpHelperResponseEntityReader;
import hygge.web.utils.http.definitions.HttpHelperRestTemplateFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 网络请求工具 默认实现
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public class DefaultHttpHelper implements HttpHelper {
    private HttpHelperRestTemplateFactory httpHelperRestTemplateFactory;
    private HttpHelperLogger httpHelperLogger;
    private HttpHelperResponseEntityReader httpHelperResponseEntityReader;

    public DefaultHttpHelper(HttpHelperRestTemplateFactory httpHelperRestTemplateFactory, HttpHelperLogger httpHelperLogger, HttpHelperResponseEntityReader httpHelperResponseEntityReader) {
        this.httpHelperRestTemplateFactory = httpHelperRestTemplateFactory;
        this.httpHelperLogger = httpHelperLogger;
        this.httpHelperResponseEntityReader = httpHelperResponseEntityReader;
    }

    @Override
    public <R, T> HttpResponse<R, T> get(String url, Class<T> targetClass) {
        return get(url, newDefaultHeaders(), null, targetClass);
    }

    public <R, T> HttpResponse<R, T> get(String url, TypeReference<T> typeReference) {
        return get(url, newDefaultHeaders(), null, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> get(String url, HttpHeaders headers, R requestObject, Class<T> targetClass) {
        return get(null, url, headers, requestObject, targetClass);
    }

    public <R, T> HttpResponse<R, T> get(String url, HttpHeaders headers, R requestObject, TypeReference<T> typeReference) {
        return get(null, url, headers, requestObject, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> get(HttpHelperRequestConfiguration configuration, String url, HttpHeaders headers, R requestObject, Class<T> targetClass) {
        return sendRequest(configuration, url, HttpMethod.GET, headers, requestObject, String.class, targetClass);
    }

    public <R, T> HttpResponse<R, T> get(HttpHelperRequestConfiguration configuration, String url, HttpHeaders headers, R requestObject, TypeReference<T> typeReference) {
        return sendRequest(configuration, url, HttpMethod.GET, headers, requestObject, String.class, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> post(String url, Class<T> targetClass) {
        return post(url, newDefaultHeaders(), null, targetClass);
    }

    public <R, T> HttpResponse<R, T> post(String url, TypeReference<T> typeReference) {
        return post(url, newDefaultHeaders(), null, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> post(String url, HttpHeaders headers, R requestObject, Class<T> targetClass) {
        return post(null, url, headers, requestObject, targetClass);
    }

    public <R, T> HttpResponse<R, T> post(String url, HttpHeaders headers, R requestObject, TypeReference<T> typeReference) {
        return post(null, url, headers, requestObject, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> post(HttpHelperRequestConfiguration configuration, String url, HttpHeaders headers, R requestObject, Class<T> targetClass) {
        return sendRequest(configuration, url, HttpMethod.POST, headers, requestObject, String.class, targetClass);
    }

    public <R, T> HttpResponse<R, T> post(HttpHelperRequestConfiguration configuration, String url, HttpHeaders headers, R requestObject, TypeReference<T> typeReference) {
        return sendRequest(configuration, url, HttpMethod.POST, headers, requestObject, String.class, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> patch(String url, Class<T> targetClass) {
        return patch(url, newDefaultHeaders(), null, targetClass);
    }

    public <R, T> HttpResponse<R, T> patch(String url, TypeReference<T> typeReference) {
        return patch(url, newDefaultHeaders(), null, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> patch(String url, HttpHeaders headers, R requestObject, Class<T> targetClass) {
        return patch(null, url, headers, requestObject, targetClass);
    }

    public <R, T> HttpResponse<R, T> patch(String url, HttpHeaders headers, R requestObject, TypeReference<T> typeReference) {
        return patch(null, url, headers, requestObject, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> patch(HttpHelperRequestConfiguration configuration, String url, HttpHeaders headers, R requestObject, Class<T> targetClass) {
        return sendRequest(configuration, url, HttpMethod.PATCH, headers, requestObject, String.class, targetClass);
    }

    public <R, T> HttpResponse<R, T> patch(HttpHelperRequestConfiguration configuration, String url, HttpHeaders headers, R requestObject, TypeReference<T> typeReference) {
        return sendRequest(configuration, url, HttpMethod.PATCH, headers, requestObject, String.class, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> put(String url, Class<T> targetClass) {
        return put(url, newDefaultHeaders(), null, targetClass);
    }

    public <R, T> HttpResponse<R, T> put(String url, TypeReference<T> typeReference) {
        return put(url, newDefaultHeaders(), null, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> put(String url, HttpHeaders headers, R requestObject, Class<T> targetClass) {
        return put(null, url, headers, requestObject, targetClass);
    }

    public <R, T> HttpResponse<R, T> put(String url, HttpHeaders headers, R requestObject, TypeReference<T> typeReference) {
        return put(null, url, headers, requestObject, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> put(HttpHelperRequestConfiguration configuration, String url, HttpHeaders headers, R requestObject, Class<T> targetClass) {
        return sendRequest(configuration, url, HttpMethod.PUT, headers, requestObject, String.class, targetClass);
    }

    public <R, T> HttpResponse<R, T> put(HttpHelperRequestConfiguration configuration, String url, HttpHeaders headers, R requestObject, TypeReference<T> typeReference) {
        return sendRequest(configuration, url, HttpMethod.PUT, headers, requestObject, String.class, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> delete(String url, Class<T> targetClass) {
        return delete(url, newDefaultHeaders(), null, targetClass);
    }

    public <R, T> HttpResponse<R, T> delete(String url, TypeReference<T> typeReference) {
        return delete(url, newDefaultHeaders(), null, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> delete(String url, HttpHeaders headers, R requestObject, Class<T> targetClass) {
        return delete(null, url, headers, requestObject, targetClass);
    }

    public <R, T> HttpResponse<R, T> delete(String url, HttpHeaders headers, R requestObject, TypeReference<T> typeReference) {
        return delete(null, url, headers, requestObject, typeReference);
    }

    @Override
    public <R, T> HttpResponse<R, T> delete(HttpHelperRequestConfiguration configuration, String url, HttpHeaders headers, R requestObject, Class<T> targetClass) {
        return sendRequest(configuration, url, HttpMethod.DELETE, headers, requestObject, String.class, targetClass);
    }

    public <R, T> HttpResponse<R, T> delete(HttpHelperRequestConfiguration configuration, String url, HttpHeaders headers, R requestObject, TypeReference<T> typeReference) {
        return sendRequest(configuration, url, HttpMethod.DELETE, headers, requestObject, String.class, typeReference);
    }

    @Override
    public <R, E, T> HttpResponse<R, T> sendRequest(HttpHelperRequestConfiguration configuration, String url, HttpMethod httpMethod, HttpHeaders requestHeaders, R requestObject, Class<E> responseClassInfo, Object dataClassInfo) {
        if (configuration == null) {
            configuration = httpHelperRestTemplateFactory.getDefaultConfiguration();
        }

        Long startTs = System.currentTimeMillis();
        HttpResponse<R, T> result = new HttpResponse<>(startTs, url, httpMethod);
        RestTemplate restTemplate = httpHelperRestTemplateFactory.getInstance(configuration);
        E originalResponse = null;

        try {
            HttpEntity<R> requestEntity = new HttpEntity<>(requestObject, requestHeaders);
            result.setRequestHeaders(requestHeaders);
            result.setRequestData(requestObject);
            ResponseEntity<E> responseEntity = restTemplate.exchange(url, httpMethod, requestEntity, responseClassInfo);
            result.initResponse(responseEntity.getStatusCode(), responseEntity.getHeaders());
            originalResponse = responseEntity.getBody();
            @SuppressWarnings("unchecked")
            T data = (T) httpHelperResponseEntityReader.readAsObjectSmart(originalResponse, dataClassInfo);
            result.setData(data);
        } catch (JsonProcessingException e) {
            result.setExceptionOccurred(true);
            if (originalResponse instanceof String) {
                result.setOriginalResponse((String) originalResponse);
            }
        } catch (Exception e) {
            result.setExceptionOccurred(true);
            throw new UtilRuntimeException("HttpHelper fail to request.", e);
        } finally {
            httpHelperLogger.logOutput(configuration, result);
        }
        return result;
    }

    public HttpHelperRestTemplateFactory getHttpHelperRestTemplateFactory() {
        return httpHelperRestTemplateFactory;
    }

    public void setHttpHelperRestTemplateFactory(HttpHelperRestTemplateFactory httpHelperRestTemplateFactory) {
        this.httpHelperRestTemplateFactory = httpHelperRestTemplateFactory;
    }

    public HttpHelperLogger getHttpHelperLogger() {
        return httpHelperLogger;
    }

    public void setHttpHelperLogger(HttpHelperLogger httpHelperLogger) {
        this.httpHelperLogger = httpHelperLogger;
    }

    public HttpHelperResponseEntityReader getHttpHelperResponseEntityReader() {
        return httpHelperResponseEntityReader;
    }

    public void setHttpHelperResponseEntityReader(HttpHelperResponseEntityReader httpHelperResponseEntityReader) {
        this.httpHelperResponseEntityReader = httpHelperResponseEntityReader;
    }
}
