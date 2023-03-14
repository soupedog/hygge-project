package hygge.web.utils.http.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hygge.util.UtilCreator;
import hygge.util.definition.JsonHelper;
import hygge.utils.json.jackson.serializer.HyggeLogInfoSerializer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

/**
 * 网络请求工具 返回值
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse<R, T> {
    protected static final JsonHelper<?> jsonHelper = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(false);

    protected Long startTs;
    protected Long cost;
    protected HttpMethod httpMethod;
    protected String url;
    protected HttpHeaders requestHeaders;
    @JsonSerialize(using = HyggeLogInfoSerializer.class)
    protected R requestData;
    protected Boolean exceptionOccurred;
    protected HttpStatus httpStatus;
    protected HttpHeaders responseHeaders;
    @JsonSerialize(using = HyggeLogInfoSerializer.class)
    protected T data;
    protected String originalResponse;

    public HttpResponse(Long startTs, String url, HttpMethod httpMethod) {
        this.startTs = startTs;
        this.url = url;
        this.httpMethod = httpMethod;
    }

    public void initResponse(HttpStatus httpStatus, HttpHeaders responseHeaders) {
        this.httpStatus = httpStatus;
        this.responseHeaders = responseHeaders;
    }

    @JsonIgnore
    public boolean isSuccess() {
        // No exceptions triggered
        return (exceptionOccurred == null || Boolean.FALSE.equals(exceptionOccurred))
                // HttpStatus is 200
                && HttpStatus.OK.equals(httpStatus);
    }

    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

    @JsonIgnore
    public boolean expected(HttpStatus... expected) {
        if (httpStatus == null) {
            return false;
        }
        for (HttpStatus item : expected) {
            if (item.equals(httpStatus)) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public <O> O getOriginalResponse(Class<O> tClass) {
        return jsonHelper.readAsObject(originalResponse, tClass);
    }

    /**
     * 请先确认 {@link JsonHelper#readAsObjectWithClassInfo(String, Object)}
     */
    @JsonIgnore
    public <O> O getOriginalResponseComplexType(Object classInfo) {
        return (O) jsonHelper.readAsObjectWithClassInfo(originalResponse, classInfo);
    }

    @Override
    public String toString() {
        return jsonHelper.formatAsString(this);
    }

    public Long getStartTs() {
        return startTs;
    }

    public void setStartTs(Long startTs) {
        this.startTs = startTs;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(HttpHeaders requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public R getRequestData() {
        return requestData;
    }

    public void setRequestData(R requestData) {
        this.requestData = requestData;
    }

    public Boolean getExceptionOccurred() {
        return exceptionOccurred;
    }

    public void setExceptionOccurred(Boolean exceptionOccurred) {
        this.exceptionOccurred = exceptionOccurred;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(HttpHeaders responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getOriginalResponse() {
        return originalResponse;
    }

    public void setOriginalResponse(String originalResponse) {
        this.originalResponse = originalResponse;
    }
}
