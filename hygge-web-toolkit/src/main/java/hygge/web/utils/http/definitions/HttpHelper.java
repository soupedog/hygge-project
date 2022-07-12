package hygge.web.utils.http.definitions;

import hygge.web.utils.http.bo.HttpResponse;
import hygge.web.utils.http.configuration.HttpHelperConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Base64;

/**
 * 网络请求工具
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public interface HttpHelper {
    default HttpHeaders newDefaultHeaders() {
        HttpHeaders result = new HttpHeaders();
        result.setContentType(MediaType.APPLICATION_JSON);
        return result;
    }

    default String getBaseAuth(String name, String password) {
        String auth = name + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        return "Basic " + encodedAuth;
    }

    default HttpHeaders newHeaderWithBasicAuth(String basicAuthString, MediaType mediaType) {
        HttpHeaders result = new HttpHeaders();
        result.setContentType(mediaType);
        result.set(HttpHeaders.AUTHORIZATION, basicAuthString);
        return result;
    }

    default HttpHeaders newHeaderWithBasicAuth(String name, String password, MediaType mediaType) {
        HttpHeaders result = new HttpHeaders();
        result.setContentType(mediaType);
        String basicAuthString = getBaseAuth(name, password);
        result.set(HttpHeaders.AUTHORIZATION, basicAuthString);
        return result;
    }

    <R, T> HttpResponse<R, T> get(String url, Class<T> targetClass);

    <R, T> HttpResponse<R, T> get(String url, HttpHeaders headers, R requestObject, Class<T> targetClass);

    <R, T> HttpResponse<R, T> get(HttpHelperConfig config, String url, HttpHeaders headers, R requestObject, Class<T> targetClass);

    <R, T> HttpResponse<R, T> post(String url, Class<T> targetClass);

    <R, T> HttpResponse<R, T> post(String url, HttpHeaders headers, R requestObject, Class<T> targetClass);

    <R, T> HttpResponse<R, T> post(HttpHelperConfig config, String url, HttpHeaders headers, R requestObject, Class<T> targetClass);

    <R, T> HttpResponse<R, T> patch(String url, Class<T> targetClass);

    <R, T> HttpResponse<R, T> patch(String url, HttpHeaders headers, R requestObject, Class<T> targetClass);

    <R, T> HttpResponse<R, T> patch(HttpHelperConfig config, String url, HttpHeaders headers, R requestObject, Class<T> targetClass);

    <R, T> HttpResponse<R, T> put(String url, Class<T> targetClass);

    <R, T> HttpResponse<R, T> put(String url, HttpHeaders headers, R requestObject, Class<T> targetClass);

    <R, T> HttpResponse<R, T> put(HttpHelperConfig config, String url, HttpHeaders headers, R requestObject, Class<T> targetClass);

    <R, T> HttpResponse<R, T> delete(String url, Class<T> targetClass);

    <R, T> HttpResponse<R, T> delete(String url, HttpHeaders headers, R requestObject, Class<T> targetClass);

    <R, T> HttpResponse<R, T> delete(HttpHelperConfig config, String url, HttpHeaders headers, R requestObject, Class<T> targetClass);

    /**
     * 如果你想下载文件:<br/>
     * HttpResponse<Void, byte[]> response = httpHelper.doRequest(null, "https://www.xxxxxxxxx/xxxxxx.png", HttpMethod.GET, headers, null, byte[].class, byte[].class);
     */
    <R, E, T> HttpResponse<R, T> sendRequest(HttpHelperConfig config, String url, HttpMethod httpMethod, HttpHeaders requestHeaders, R requestObject, Class<E> responseClassInfo, Object dataClassInfo);
}
