package hygge.utils.definitions;

import hygge.utils.HyggeUtil;

/**
 * 随机唯一标识生成器
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface RandomUniqueGenerator extends HyggeUtil {
    enum ConfigKey {
        /**
         * 起始 UTC 毫秒级时间戳
         */
        START_TS("startTs"),
        /**
         * 第一部分位数
         */
        PART1_LENGTH("part1Length"),
        /**
         * 第二部分位数
         */
        PART2_LENGTH("part2Length"),
        /**
         * 自增序列部分位数
         */
        SEQUENCE_PART_LENGTH("sequencePartLength"),
        /**
         * 第一部分实际值
         */
        PART1_VAL("part1Val"),
        /**
         * 第二部分实际值
         */
        PART2_VAL("part2Val");

        private final String description;

        ConfigKey(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 返回一个不重复的数字 id
     *
     * @return 不重复的数字 id
     */
    long createKey();
}
