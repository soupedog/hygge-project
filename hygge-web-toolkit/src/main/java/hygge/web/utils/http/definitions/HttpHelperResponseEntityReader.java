package hygge.web.utils.http.definitions;

/**
 * 网络请求工具 Response 读取工具
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public interface HttpHelperResponseEntityReader {

    <T> T readAsObject(String originalResponse, Class<T> tClass);

    <E> Object readAsObjectSmart(E originalResponse, Object dataClassInfo) throws Exception;
}
