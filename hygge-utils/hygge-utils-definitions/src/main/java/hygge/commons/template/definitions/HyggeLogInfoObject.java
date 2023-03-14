package hygge.commons.template.definitions;

/**
 * 标记当前对象为日志对象，提供了生成自定义日志(用于日志信息的脱敏、剔除冗余信息等操作)的专有方法
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public interface HyggeLogInfoObject {
    /**
     * 将当前对象转化为自定义内容的 json 格式日志信息
     */
    String toJsonLogInfo();
}
