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

import com.fasterxml.jackson.databind.ObjectMapper;
import hygge.util.UtilCreator;
import hygge.util.definition.JsonHelper;
import hygge.util.definition.ParameterHelper;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Xavier
 * @date 2026/6/30
 */
public abstract class BaseHyggeEvent<S> extends ApplicationEvent {
    protected static final JsonHelper<ObjectMapper> jsonHelper = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(false);
    protected static final ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
    /**
     * 处理过程是否为异步进行，默认是同步。
     */
    protected boolean isAsynchronous;
    protected String hyggeTraceRoot;
    protected String hyggeTraceInfo;
    protected int stepCount;

    protected BaseHyggeEvent(S source) {
        super(source);
    }

    protected BaseHyggeEvent(S source, Clock clock) {
        super(source, clock);
    }

    @SuppressWarnings("unchecked")
    public S getActualSource() {
        return (S) source;
    }

    public boolean isAsynchronous() {
        return isAsynchronous;
    }

    public void setAsynchronous(boolean asynchronous) {
        isAsynchronous = asynchronous;
    }

    public String getHyggeTraceRoot() {
        return hyggeTraceRoot;
    }

    public void setHyggeTraceRoot(String hyggeTraceRoot) {
        this.hyggeTraceRoot = hyggeTraceRoot;
    }

    public String getHyggeTraceInfo() {
        return hyggeTraceInfo;
    }

    public void setHyggeTraceInfo(String hyggeTraceInfo) {
        this.hyggeTraceInfo = hyggeTraceInfo;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    /**
     * {@link BaseHyggeEvent#setTrigger(String, int, String)} 的语法糖
     */
    public void setTrigger(BaseHyggeEvent<S> event) {
        setTrigger(event.hyggeTraceRoot, event.getStepCount(), event.hyggeTraceInfo);
    }

    /**
     * @param hyggeTraceRoot 请求唯一标识
     * @param stepCount      同线程内共享的步骤计数器
     * @param traceInfo      线程切换位点记录
     */
    public void setTrigger(String hyggeTraceRoot, int stepCount, String traceInfo) {
        if (isAsynchronous) {
            // 异步执行事件
            String triggerStep = String.valueOf(stepCount);

            if (parameterHelper.isEmpty(traceInfo)) {
                traceInfo = triggerStep;
            } else {
                traceInfo = traceInfo.concat("-").concat(triggerStep);
            }

            this.stepCount = 0;
        } else {
            // 同步执行事件
            this.stepCount = stepCount;
        }

        // 同步、异步均处理的属性
        this.hyggeTraceRoot = hyggeTraceRoot;
        this.hyggeTraceInfo = traceInfo;
    }

    public String toJsonInfo() {
        Map<String, Object> resultTemp = toInfoMap();
        return jsonHelper.formatAsString(resultTemp);
    }

    public Map<String, Object> toInfoMap() {
        Map<String, Object> resultTemp = new LinkedHashMap<>();
        resultTemp.put("ts", getTimestamp());
        if (isAsynchronous) {
            resultTemp.put("async", isAsynchronous);
        }
        resultTemp.put("source", getActualSource());
        if (parameterHelper.isNotEmpty(hyggeTraceInfo)) {
            resultTemp.put("trace", hyggeTraceInfo);
        }
        if (parameterHelper.isNotEmpty(stepCount)) {
            resultTemp.put("step", stepCount);
        }
        return resultTemp;
    }
}
