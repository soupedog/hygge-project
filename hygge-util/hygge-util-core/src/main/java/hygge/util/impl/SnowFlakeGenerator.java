/*
 * Copyright 2022-2024 the original author or authors.
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

package hygge.util.impl;


import hygge.commons.constant.enums.DateTimeFormatModeEnum;
import hygge.commons.exception.UtilRuntimeException;
import hygge.util.UtilCreator;
import hygge.util.definition.ParameterHelper;
import hygge.util.definition.RandomUniqueGenerator;
import hygge.util.definition.TimeHelper;

import java.util.Properties;

import static hygge.util.impl.SnowFlakeGenerator.ConfigKey.PART1_LENGTH;
import static hygge.util.impl.SnowFlakeGenerator.ConfigKey.PART1_VAL;
import static hygge.util.impl.SnowFlakeGenerator.ConfigKey.PART2_LENGTH;
import static hygge.util.impl.SnowFlakeGenerator.ConfigKey.PART2_VAL;
import static hygge.util.impl.SnowFlakeGenerator.ConfigKey.SEQUENCE_PART_LENGTH;
import static hygge.util.impl.SnowFlakeGenerator.ConfigKey.START_TS;

/**
 * 通用的雪花算法 id 生成器<br/>
 * 样例模板：64 位除去最左边的正负符号位，右侧可自定义的 63 位 = (part1 + part2 + sequencePart + tsPart)
 * 0 - part1 - part2 - sequencePart - tsPart
 *
 * @author Xavier
 * @date 2022/7/9
 * @since 1.0
 */
public class SnowFlakeGenerator implements RandomUniqueGenerator {
    protected ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
    protected TimeHelper timeHelper = UtilCreator.INSTANCE.getDefaultInstance(TimeHelper.class);
    /**
     * 可自定义部分长度
     */
    private static final int CUSTOM_LENGTH = 63;
    /**
     * 起始时间戳
     */
    private long startTs;
    /**
     * 会出现重复的最早时间
     */
    private long endTs;
    /**
     * 左起第一部分 位数
     */
    private int part1Length;
    /**
     * 左起第二部分 位数
     */
    private int part2Length;
    /**
     * 自增序列部分 位数
     */
    private int sequencePartLength;
    /**
     * 自增序列部分 位数
     */
    private int tsPartLength;
    /**
     * 左起第一部分 最大值
     */
    private long part1MaxVal;
    /**
     * 左起第一部分 实际值
     */
    private long part1Val;
    /**
     * 左起第二部分 最大值
     */
    private long part2MaxVal;
    /**
     * 左起第二部分 实际值
     */
    private long part2Val;
    /**
     * 自增序列部分 最大值
     */
    private long sequencePartMaxVal;
    /**
     * 左起第一部分 按位或 目标
     */
    private long part1OrTarget;
    /**
     * 左起第二部分 按位或 目标
     */
    private long part2OrTarget;
    /**
     * 左起稳定部分 按位或 目标
     */
    private long stablePartOrTarget;
    /**
     * 自增计数器
     */
    private long sequence = 0;
    /**
     * 上一个时间戳
     */
    private long lastTs = -1L;

    public SnowFlakeGenerator() {
        Properties properties = createDefaultConfig();

        parameterHelper.objectNotNull(properties, "Unexpected properties,it can't be null,or you can use SnowFlakeGenerator.createDefaultConfig.");
        Long startTs = parameterHelper.longFormatNotEmpty(START_TS.getDescription(), properties.get(START_TS));
        int part1Length = parameterHelper.integerFormatNotEmpty(PART1_LENGTH.getDescription(), properties.get(PART1_LENGTH));
        int part2Length = parameterHelper.integerFormatNotEmpty(PART2_LENGTH.getDescription(), properties.get(PART2_LENGTH));
        int sequencePartLength = parameterHelper.integerFormatNotEmpty(SEQUENCE_PART_LENGTH.getDescription(), properties.get(SEQUENCE_PART_LENGTH));
        long part1Val = parameterHelper.longFormatNotEmpty(PART1_VAL.getDescription(), properties.get(PART1_VAL));
        long part2Val = parameterHelper.longFormatNotEmpty(PART2_VAL.getDescription(), properties.get(PART2_VAL));

        this.startTs = startTs;
        this.part1Length = parameterHelper.integerFormatNotEmpty("part1Length", part1Length, 1, 60);
        this.part2Length = parameterHelper.integerFormatNotEmpty("part2Length", part2Length, 1, 60);
        this.sequencePartLength = parameterHelper.integerFormatNotEmpty("sequencePartLength", sequencePartLength, 1, 60);
        this.tsPartLength = CUSTOM_LENGTH - parameterHelper.integerFormat("part1Length + part2Length + sequencePartLength", (part1Length + part2Length + sequencePartLength), 3, 62);
        // this.endTs = this.startTs + (2 ^ this.tsPartLength - 1);
        this.endTs = this.startTs + ~(-1L << this.tsPartLength);
        // 2 ^ this.part1Length - 1;
        this.part1MaxVal = ~(-1L << this.part1Length);
        // 2 ^ this.part2Length - 1;
        this.part2MaxVal = ~(-1L << this.part2Length);
        // 2 ^ this.sequencePartLength - 1;
        this.sequencePartMaxVal = ~(-1L << this.sequencePartLength);
        init(part1Val, part2Val);
    }

    public SnowFlakeGenerator(Long startTs, int part1Length, int part2Length, int sequencePartLength, long part1Val, long part2Val) {
        this.startTs = parameterHelper.longFormatOfNullable("startTs", startTs, System.currentTimeMillis());
        this.part1Length = parameterHelper.integerFormatNotEmpty("part1Length", part1Length, 1, 60);
        this.part2Length = parameterHelper.integerFormatNotEmpty("part2Length", part2Length, 1, 60);
        this.sequencePartLength = parameterHelper.integerFormatNotEmpty("sequencePartLength", sequencePartLength, 1, 60);
        this.tsPartLength = CUSTOM_LENGTH - parameterHelper.integerFormat("part1Length + part2Length + sequencePartLength", (part1Length + part2Length + sequencePartLength), 3, 62);
        // this.endTs = this.startTs + (2 ^ this.tsPartLength - 1);
        this.endTs = this.startTs + ~(-1L << this.tsPartLength);
        // 2 ^ this.part1Length - 1;
        this.part1MaxVal = ~(-1L << this.part1Length);
        // 2 ^ this.part2Length - 1;
        this.part2MaxVal = ~(-1L << this.part2Length);
        // 2 ^ this.sequencePartLength - 1;
        this.sequencePartMaxVal = ~(-1L << this.sequencePartLength);
        init(part1Val, part2Val);
    }

    public SnowFlakeGenerator(Properties properties) {
        parameterHelper.objectNotNull(properties, "Unexpected properties,it can't be null,or you can use SnowFlakeGenerator.createDefaultConfig.");
        Long startTs = parameterHelper.longFormatNotEmpty(START_TS.getDescription(), properties.get(START_TS));
        int part1Length = parameterHelper.integerFormatNotEmpty(PART1_LENGTH.getDescription(), properties.get(PART1_LENGTH));
        int part2Length = parameterHelper.integerFormatNotEmpty(PART2_LENGTH.getDescription(), properties.get(PART2_LENGTH));
        int sequencePartLength = parameterHelper.integerFormatNotEmpty(SEQUENCE_PART_LENGTH.getDescription(), properties.get(SEQUENCE_PART_LENGTH));
        long part1Val = parameterHelper.longFormatNotEmpty(PART1_VAL.getDescription(), properties.get(PART1_VAL));
        long part2Val = parameterHelper.longFormatNotEmpty(PART2_VAL.getDescription(), properties.get(PART2_VAL));

        this.startTs = startTs;
        this.part1Length = parameterHelper.integerFormatNotEmpty("part1Length", part1Length, 1, 60);
        this.part2Length = parameterHelper.integerFormatNotEmpty("part2Length", part2Length, 1, 60);
        this.sequencePartLength = parameterHelper.integerFormatNotEmpty("sequencePartLength", sequencePartLength, 1, 60);
        this.tsPartLength = CUSTOM_LENGTH - parameterHelper.integerFormat("part1Length + part2Length + sequencePartLength", (part1Length + part2Length + sequencePartLength), 3, 62);
        // this.endTs = this.startTs + (2 ^ this.tsPartLength - 1);
        this.endTs = this.startTs + ~(-1L << this.tsPartLength);
        // 2 ^ this.part1Length - 1;
        this.part1MaxVal = ~(-1L << this.part1Length);
        // 2 ^ this.part2Length - 1;
        this.part2MaxVal = ~(-1L << this.part2Length);
        // 2 ^ this.sequencePartLength - 1;
        this.sequencePartMaxVal = ~(-1L << this.sequencePartLength);
        init(part1Val, part2Val);
    }

    private void init(long part1Val, long part2Val) {
        this.part1OrTarget = parameterHelper.longFormatNotEmpty("part1Val", part1Val, 0L, part1MaxVal)
                << (tsPartLength + sequencePartLength + part2Length);
        this.part1Val = part1Val;
        this.part2OrTarget = parameterHelper.longFormatNotEmpty("part2Val", part2Val, 0L, part2MaxVal)
                << (tsPartLength + sequencePartLength);
        this.part2Val = part2Val;
        this.stablePartOrTarget = part1OrTarget | part2OrTarget;
    }

    /**
     * 创建一份默认配置的
     *
     * @return 默认的配置项
     */
    public static Properties createDefaultConfig() {
        Properties result = new Properties();
        result.put(START_TS, 803966400000L);
        result.put(PART1_LENGTH, 2);
        result.put(PART2_LENGTH, 5);
        result.put(SEQUENCE_PART_LENGTH, 12);
        result.put(PART1_VAL, 0L);
        result.put(PART2_VAL, 0L);
        return result;
    }

    @Override
    public synchronized long createKey() {
        long currentTs = System.currentTimeMillis();
        if (currentTs > endTs) {
            throw new UtilRuntimeException(String.format("SnowFlakeGenerator exhaustion %s .", this));
        }
        long result;
        if (currentTs == lastTs) {
            if (sequence > sequencePartMaxVal) {
                sequence = 0;
                currentTs = blockToNextTs(currentTs);
            }
            result = calculateKey(currentTs - startTs);
            lastTs = currentTs;
        } else {
            if (currentTs > lastTs) {
                sequence = 0;
                result = calculateKey(currentTs - startTs);
                lastTs = currentTs;
            } else {
                // 时间发生回滚
                throw new UtilRuntimeException(String.format("SnowFlakeGenerator back in time,currentTs:%d lastTs:%d.", currentTs, lastTs));
            }
        }
        return result;
    }

    /**
     * 自旋阻塞到下一个时间戳
     *
     * @param currentTs 当前时间戳
     * @return 下一个时间戳
     */
    private long blockToNextTs(long currentTs) {
        long result = System.currentTimeMillis();
        while (result <= currentTs) {
            result = System.currentTimeMillis();
        }
        return result;
    }

    /**
     * 运算出一个 key
     */
    private long calculateKey(long tsPart) {
        long result = stablePartOrTarget
                | sequence << tsPartLength
                | tsPart;
        sequence += 1;
        return result;
    }

    @Override
    public String toString() {
        return String.format("{\"stablePart\":\"%s\",\"startTs\":%s,\"endTs\":%s,\"sequencePartLength\":%d,\"part1Val\":%d,\"part2Val\":%d}",
                Long.toBinaryString(stablePartOrTarget),
                timeHelper.format(startTs, DateTimeFormatModeEnum.FULL_TRIM),
                timeHelper.format(endTs, DateTimeFormatModeEnum.FULL_TRIM),
                sequencePartLength,
                part1Val,
                part2Val
        );
    }

    /**
     * 返回起始时间 UTC 时间戳
     */
    public long getStartTs() {
        return startTs;
    }

    /**
     * 返回最早出现重复的 UTC 时间戳
     */
    public long getEndTs() {
        return endTs;
    }

    public enum ConfigKey {
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
}
