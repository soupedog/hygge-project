package hygge.service.client;

import hygge.domain.RestApiRequest;
import hygge.domain.RestApiResponse;
import hygge.web.utils.http.bo.HttpResponse;
import hygge.web.utils.http.configuration.HttpHelperRequestConfiguration;
import hygge.web.utils.http.configuration.inner.HttpLogType;
import hygge.web.utils.http.impl.DefaultHttpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Xavier
 * @date 2023/1/12
 */
@Component
public class RestClient {
    @Autowired
    private DefaultHttpHelper httpHelper;

    public void requestGetApi() {
        HttpResponse<Void, RestApiResponse> responseTemp = httpHelper.get("https://www.testapi.com", RestApiResponse.class);

        if (responseTemp.isSuccess()) {
            // 如果 HTTP Status Code 是 200
            RestApiResponse response = responseTemp.getData();

        } else {
            // 如果 HTTP Status Code 不是 200
        }
    }

    public void requestPostApi() {
        HttpResponse<RestApiRequest, RestApiResponse> responseTemp = httpHelper.post("https://www.testapi.com",
                httpHelper.newHeaderWithBasicAuth("name", "password", MediaType.APPLICATION_JSON),
                new RestApiRequest(),
                RestApiResponse.class);

        if (responseTemp.expected(HttpStatus.OK, HttpStatus.ACCEPTED)) {
            // 如果 HTTP Status Code 是 200/202 的任意一种
            RestApiResponse response = responseTemp.getData();
        } else {
            // 如果 HTTP Status Code 不是 200/202 的任意一种
        }
    }

    public void requestWithConfiguration() {
        // 改接口关闭出入参日志，忽略 https 证书验证，连接、读取超时时间均为 1 s
        HttpHelperRequestConfiguration configuration = new HttpHelperRequestConfiguration(HttpLogType.NONE, true, 1000, 1000);

        HttpResponse<RestApiRequest, RestApiResponse> responseTemp = httpHelper.sendRequest(configuration, "https://www.testapi.com",
                HttpMethod.PUT,
                httpHelper.newDefaultHeaders(),
                new RestApiRequest(),
                String.class,
                RestApiResponse.class);
    }

    public void downloadFile() {
        HttpResponse<Void, byte[]> response = httpHelper.sendRequest(null, "https://www.testapi/test.png", HttpMethod.GET, httpHelper.newDefaultHeaders(), null, byte[].class, byte[].class);
    }

    public void uploadFile() {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        // requestBody 赋值过程省略，和 RestTemplate resource 方式完全一致

        // 改接口关闭出入参日志，忽略 https 证书验证，连接、读取超时时间均为 1 s
        HttpHelperRequestConfiguration configuration = new HttpHelperRequestConfiguration(HttpLogType.NONE, true, 1000, 1000);

        HttpResponse<MultiValueMap<String, Object>, RestApiResponse> responseTemp = httpHelper.sendRequest(configuration, "https://www.testapi.com", HttpMethod.POST, httpHelper.newDefaultHeaders(), requestBody, String.class, RestApiResponse.class);
    }

}
