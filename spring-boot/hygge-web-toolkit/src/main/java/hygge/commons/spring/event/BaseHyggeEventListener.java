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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Xavier
 * @date 2026/6/30
 */
public abstract class BaseHyggeEventListener<T extends BaseHyggeEvent<?>> implements ApplicationListener<T> {
    private static final Logger log = LoggerFactory.getLogger(BaseHyggeEventListener.class);

    @Override
    public void onApplicationEvent(T event) {
        if (event.isAsynchronous()) {
            asynchronousProcess(event);
        } else {
            onStart(event);
            recordEventInfo(event);
            handleEvent(event);
        }
    }

    private void asynchronousProcess(T event) {
        Map<String, String> contextMapFromParent = MDC.getCopyOfContextMap();

        CompletableFuture.runAsync(() -> {
            initMDCForSubThread(contextMapFromParent);

            onStart(event);
            recordEventInfo(event);
            handleEvent(event);
        }).handle((result, throwable) -> {
            try {
                // 存在异常
                if (throwable != null) {
                    handleThrowableForSubThread(throwable);
                }
            } finally {
                // 清扫子线程 MDC
                clearMDC();
            }
            return null;
        });
    }

    /**
     * 获取当前监听器名称，用于自动日志记录
     */
    protected abstract String getListenerName();

    protected void onStart(T event) {
        event.getStepCount().addAndGet(1);
    }

    /**
     * 仅针对需要异步处理的 event 进行子线程 MDC 配置
     *
     * @param contextMapFromParent 父辈线程的 MDC 配置项
     */
    protected void initMDCForSubThread(Map<String, String> contextMapFromParent) {
        if (contextMapFromParent != null) {
            MDC.setContextMap(contextMapFromParent);
        }
    }

    /**
     * 处理事件的具体方法。执行该方法前会先自动执行
     */
    protected abstract void handleEvent(T event);

    /**
     * 仅针对需要异步处理的 event 的异常处理机制。<br/>
     * 默认行为是打印异常日志
     *
     * @param throwable 运行过程中抛出的异常
     */
    protected void handleThrowableForSubThread(Throwable throwable) {
        String info = String.format("%s fail to consume event.", getListenerName());
        log.error(info, throwable);
    }

    protected void clearMDC() {
        MDC.clear();
    }

    /**
     * 仅在接受到
     */
    protected void recordEventInfo(T event) {
        log.info("{} receive:{}",
                getListenerName(),
                event.toJsonInfo()
        );
    }
}