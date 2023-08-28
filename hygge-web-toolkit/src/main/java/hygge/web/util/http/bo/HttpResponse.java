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

package hygge.web.util.http.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hygge.util.UtilCreator;
import hygge.util.definition.JsonHelper;
import hygge.util.json.jackson.serializer.HyggeLogInfoSerializer;
import org.springframework.http.HttpHeaders;

/**
 * 网络请求工具 返回值
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse<R, T> {
    public static final Integer HTTP_STATUS_OK = 200;
    protected static final JsonHelper<?> jsonHelper = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(false);

    protected Long startTs;
    protected Long cost;
    /**
     * 因为 spring 6.x 不向前兼容的改动，为了 spring boot 3.x 的兼容性，此处降级为字符串类型
     * <p>
     * {@link org.springframework.http.HttpMethod} 在 spring 6.x 中不再是枚举类
     */
    protected String httpMethod;
    protected String url;
    protected HttpHeaders requestHeaders;
    @JsonSerialize(using = HyggeLogInfoSerializer.class)
    protected R requestData;
    protected Boolean exceptionOccurred;
    /**
     * 因为 spring 6.x 不向前兼容的改动，为了 spring boot 3.x 的兼容性，此处降级为数字类型
     *
     * @see org.springframework.http.HttpStatus
     * @see org.springframework.http.HttpStatusCode
     */
    protected Integer httpStatus;
    protected HttpHeaders responseHeaders;
    @JsonSerialize(using = HyggeLogInfoSerializer.class)
    protected T data;
    protected String originalResponse;

    public HttpResponse(Long startTs, String url, String httpMethod) {
        this.startTs = startTs;
        this.url = url;
        this.httpMethod = httpMethod;
    }

    public void initResponse(Integer httpStatus, HttpHeaders responseHeaders) {
        this.httpStatus = httpStatus;
        this.responseHeaders = responseHeaders;
    }

    @JsonIgnore
    public boolean isSuccess() {
        // No exceptions triggered
        return (exceptionOccurred == null || Boolean.FALSE.equals(exceptionOccurred))
                // HttpStatus is 200
                && HTTP_STATUS_OK.equals(httpStatus);
    }

    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

    /**
     * 为了 Spring 6.x 的兼容性，此处收到波及，入参不够明确。推荐入参写法：
     * <pre>
     *     expected(HttpStatus.OK.value(), HttpStatus.ACCEPTED.value())
     * </pre>
     */
    @JsonIgnore
    public boolean expected(Integer... expected) {
        if (httpStatus == null) {
            return false;
        }
        for (Integer item : expected) {
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

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
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

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
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
