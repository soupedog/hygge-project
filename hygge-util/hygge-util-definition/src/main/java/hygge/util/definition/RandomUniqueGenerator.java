package hygge.util.definition;

/**
 * 随机唯一标识生成器
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface RandomUniqueGenerator extends HyggeUtil {
    /**
     * 此处默认指向 {@link hygge.utils.impl.SnowFlakeGenerator}
     */
    @Override
    default String getHyggeName() {
        return "SnowFlakeGenerator";
    }

    /**
     * 返回一个不重复的数字 id
     *
     * @return 不重复的数字 id
     */
    long createKey();
}
