package hygge.web.utils.http.configuration.inner;

/**
 * 网络请求工具 日志格式 类型
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public enum HttpLogType {
    /**
     * 不输出日志
     */
    NONE,
    /**
     * 标准
     */
    STANDARD,
    /**
     * 标准的基础上去除 response 的 Headers
     */
    NO_RESPONSE_HEADERS,
    /**
     * 标准的基础上去除 request、response 的 Headers
     */
    NO_HEADERS,
    /**
     * 标准的基础上去除 request、response 的 body
     */
    NO_BODY,
    /**
     * 标准的基础上去除 request、response 的 Headers 与 body
     */
    NO_HEADERS_BODY,
    ;
}
