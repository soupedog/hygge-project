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

import static hygge.util.impl.SnowFlakeGenerator.ConfigKey.IDENTITY_LENGTH;
import static hygge.util.impl.SnowFlakeGenerator.ConfigKey.IDENTITY_VAL;
import static hygge.util.impl.SnowFlakeGenerator.ConfigKey.SEQUENCE_PART_LENGTH;
import static hygge.util.impl.SnowFlakeGenerator.ConfigKey.START_TS;

/**
 * 通用的雪花算法 id 生成器<br/>
 * 样例模板：64 位除去最左边的正负符号位，右侧可自定义的 63 位 = (tsPart + identity + sequencePart)
 * <br/>0 - tsPart - identity - sequencePart
 * <br/>
 * <br/>
 * tip: 相较 2026.4.9 之前不保证严格自增的版本，性能下降约 50%
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
     * 时间 位数
     */
    private int tsPartLength;
    /**
     * 生成节点标识位 位数
     */
    private int identityLength;
    /**
     * 生成节点标识位 最大值
     */
    private long identityMaxVal;
    /**
     * 自增序列部分 位数
     */
    private int sequencePartLength;
    /**
     * 自增序列部分 最大值
     */
    private long sequencePartMaxVal;
    /**
     * 生成节点标识位 实际值
     */
    private long identityVal;
    /**
     * 时间部分位移 偏移量
     */
    private long tsShift;
    /**
     * 生成节点标识位 按位或 目标
     */
    private long identityOrTarget;
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
        initWithProperties(properties);
    }

    public SnowFlakeGenerator(Properties properties) {
        initWithProperties(properties);
    }

    public SnowFlakeGenerator(Long startTs, int identityLength, int identityVal, int sequencePartLength) {
        Properties properties = new Properties();
        properties.put(START_TS, startTs);
        properties.put(IDENTITY_LENGTH, identityLength);
        properties.put(IDENTITY_VAL, identityVal);
        properties.put(SEQUENCE_PART_LENGTH, sequencePartLength);

        initWithProperties(properties);
    }

    private void initWithProperties(Properties properties) {
        parameterHelper.objectNotNull(properties, "Unexpected properties,it can't be null,or you can use SnowFlakeGenerator.createDefaultConfig.");
        this.identityLength = parameterHelper.integerFormatNotEmpty(IDENTITY_LENGTH.getDescription(), properties.get(IDENTITY_LENGTH), 1, 61);
        this.sequencePartLength = parameterHelper.integerFormatNotEmpty(SEQUENCE_PART_LENGTH.getDescription(), properties.get(SEQUENCE_PART_LENGTH), 1, 61);
        this.tsPartLength = CUSTOM_LENGTH - parameterHelper.integerFormat("identityLength + sequencePartLength", (identityLength + sequencePartLength), 2, 62);
        this.startTs = parameterHelper.longFormatNotEmpty(START_TS.getDescription(), properties.get(START_TS));

        // this.endTs = this.startTs + (2 ^ this.tsPartLength - 1);
        this.endTs = this.startTs + ~(-1L << this.tsPartLength);
        // 2 ^ this.part1Length - 1;
        this.identityMaxVal = ~(-1L << this.identityLength);
        // 2 ^ this.sequencePartLength - 1;
        this.sequencePartMaxVal = ~(-1L << this.sequencePartLength);

        this.tsShift = identityLength + sequencePartLength;
        this.identityVal = parameterHelper.longFormatNotEmpty("identityVal", identityVal, 0L, identityMaxVal);
        this.identityOrTarget = identityVal << (sequencePartLength);
    }

    /**
     * 创建一份默认配置的
     *
     * @return 默认的配置项
     */
    public static Properties createDefaultConfig() {
        Properties result = new Properties();
        result.put(START_TS, 803966400000L);
        result.put(IDENTITY_LENGTH, 5);
        result.put(IDENTITY_VAL, 0L);
        result.put(SEQUENCE_PART_LENGTH, 12);
        return result;
    }

    @Override
    public synchronized long createKey() {
        long currentTs = System.currentTimeMillis();
        // 当前时间已超过最大截止时间，说明 id 用尽了，无法再生成无重复 id
        if (currentTs > endTs) {
            throw new UtilRuntimeException(String.format("SnowFlakeGenerator exhaustion %s .", this));
        }

        if (lastTs > currentTs) {
            // 系统不定期与时间服务器通信同步时间，可能存在时间回滚
            if (lastTs - currentTs <= 5L) {
                // 回拨 5 毫秒以内，进行自旋等待到最后一次生成 id 时间，提高可用性
                currentTs = blockTo(lastTs);
            } else {
                // 误差过大作为异常处理
                throw new UtilRuntimeException(String.format("SnowFlakeGenerator back in time,currentTs:%d lastTs:%d.", currentTs, lastTs));
            }
        }

        long result;

        if (currentTs == lastTs) {
            // 同毫秒内自增序列已耗尽
            if (sequence > sequencePartMaxVal) {
                // 重置自增序列并至少阻塞的下一毫秒
                sequence = 0;
                currentTs = blockTo(currentTs + 1L);
            }
            // 同毫秒内自增序列未耗尽直接生成 id
            result = calculateKey(currentTs - startTs);
        } else if (currentTs > lastTs) {
            // 时间戳发生变化时重置自增序列
            sequence = 0;
            result = calculateKey(currentTs - startTs);
        } else {
            // 上方处理过后一定有 currentTs >= lastTs，这里不可能触发
            throw new UtilRuntimeException("Dead Code.");
        }

        // 生成一次 id 更新一下最后 id 对应的时间戳
        lastTs = currentTs;
        return result;
    }

    /**
     * 自旋阻塞到目标时间戳
     *
     * @param targetTs 目标时间戳
     * @return 阻塞结束对应的时间戳
     */
    private long blockTo(long targetTs) {
        long currentTs = System.currentTimeMillis();
        while (currentTs <= targetTs) {
            currentTs = System.currentTimeMillis();
        }
        return currentTs;
    }

    /**
     * 运算出一个 key
     */
    private long calculateKey(long tsPart) {
        long result = tsPart << tsShift
                | identityOrTarget
                | sequence;
        sequence += 1;
        return result;
    }

    @Override
    public String toString() {
        return String.format("{\"startTs\":%s,\"endTs\":%s,\"identityVal\":%d,\"sequencePartLength\":%d}",
                timeHelper.format(startTs, DateTimeFormatModeEnum.FULL_TRIM),
                timeHelper.format(endTs, DateTimeFormatModeEnum.FULL_TRIM),
                identityVal,
                sequencePartLength
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
         * 生成节点标识位位数
         */
        IDENTITY_LENGTH("identityLength"),
        /**
         * 自增序列部分位数
         */
        SEQUENCE_PART_LENGTH("sequencePartLength"),
        /**
         * 第一部分实际值
         */
        IDENTITY_VAL("identityVal"),
        ;

        private final String description;

        ConfigKey(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
