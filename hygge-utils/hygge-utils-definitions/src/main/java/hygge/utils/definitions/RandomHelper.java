package hygge.utils.definitions;

import hygge.commons.enums.StringCategoryEnum;
import hygge.utils.HyggeUtil;

/**
 * 随机生成工具类
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface RandomHelper extends HyggeUtil {
    /**
     * 生成随机数字(闭区间)
     *
     * @param minValue 随机值最小值
     * @param maxValue 随机值最大值
     * @return 一个随机整数
     */
    int getRandomInteger(int minValue, int maxValue);

    /**
     * 可重复地生成随机字符串
     *
     * @param size           随机字符串目标长度
     * @param stringCategory 随机字符池类型
     * @return 随机字符串
     */
    String getRandomString(int size, StringCategoryEnum... stringCategory);

    /**
     * 生成 UUID
     *
     * @param withOutSpecialCharacters 是否包含特殊字符 "-"
     * @return UUID
     */
    String getUniversallyUniqueIdentifier(boolean withOutSpecialCharacters);
}
