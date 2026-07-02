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

package hygge.commons.spring.event;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.function.BiFunction;

import static hygge.commons.spring.event.BaseHyggeEvent.parameterHelper;

/**
 * 低版本 JDK 构造函数 super 语法限制下的 workaround
 *
 * @author Xavier
 * @date 2026/7/1
 */
public class DefaultHyggeEventBuilder<T, R extends BaseHyggeEvent<T>> {
    protected T source;
    protected Long timestamp;
    protected boolean asynchronous;
    protected String hyggeTraceRoot;
    protected String hyggeTraceInfo;
    protected int stepCount;
    protected final BiFunction<T, Clock, R> construct;

    public DefaultHyggeEventBuilder(BiFunction<T, Clock, R> construct, T source) {
        this.construct = construct;
        this.source = source;
    }

    public DefaultHyggeEventBuilder<T, R> source(T source) {
        this.source = source;
        return this;
    }

    public DefaultHyggeEventBuilder<T, R> timestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public DefaultHyggeEventBuilder<T, R> asynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
        return this;
    }

    public DefaultHyggeEventBuilder<T, R> hyggeTraceRoot(String hyggeTraceRoot) {
        this.hyggeTraceRoot = hyggeTraceRoot;
        return this;
    }

    public DefaultHyggeEventBuilder<T, R> hyggeTraceInfo(String hyggeTraceInfo) {
        this.hyggeTraceInfo = hyggeTraceInfo;
        return this;
    }

    public DefaultHyggeEventBuilder<T, R> stepCount(int stepCount) {
        this.stepCount = stepCount;
        return this;
    }

    public R build() {
        if (timestamp == null) {
            timestamp = System.currentTimeMillis();
        }

        Instant instant = Instant.ofEpochMilli(timestamp);

        // 底层都是 UTC 时间戳，此处 ZoneId 只是用于描述在显示时，使用什么时区
        // 对 Clock 获取 long 型时间戳无影响，只是函数要求必须存在
        Clock clock = Clock.fixed(instant, ZoneId.systemDefault());

        R event = construct.apply(source, clock);

        // 设置额外属性
        event.setAsynchronous(asynchronous);

        if (parameterHelper.isNotEmpty(hyggeTraceRoot)) {
            event.setHyggeTraceInfo(hyggeTraceRoot);
        }
        if (parameterHelper.isNotEmpty(hyggeTraceInfo)) {
            event.setHyggeTraceInfo(hyggeTraceInfo);
        }
        event.setStepCount(stepCount);

        return event;
    }
}
