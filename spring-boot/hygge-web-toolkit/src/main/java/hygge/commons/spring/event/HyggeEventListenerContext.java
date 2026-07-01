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

import hygge.commons.template.container.base.AbstractInterfaceKeyHyggeContext;

import java.util.Map;

/**
 * @author Xavier
 * @date 2026/7/1
 */
public class HyggeEventListenerContext<S, E extends BaseHyggeEvent<S>> extends AbstractInterfaceKeyHyggeContext<Enum<?>, HyggeEventListenerContextKey> {
    private final long startTs;
    private Throwable throwable;
    /**
     * 用于日志输出的信息
     */
    private Map<String,Object> rowEventInfo;
    private E event;

    public HyggeEventListenerContext() {
        this.startTs = System.currentTimeMillis();
    }

    public long getStartTs() {
        return startTs;
    }

    public boolean isExceptionOccurred() {
        return throwable != null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Map<String, Object> getRowEventInfo() {
        return rowEventInfo;
    }

    public void setRowEventInfo(Map<String, Object> rowEventInfo) {
        this.rowEventInfo = rowEventInfo;
    }

    public E getEvent() {
        return event;
    }

    public void setEvent(E event) {
        this.event = event;
    }
}
