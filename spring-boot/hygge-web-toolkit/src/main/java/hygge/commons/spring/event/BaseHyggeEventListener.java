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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 注意：异步执行的 Event 该基类会自动清扫 MDC，而同步执行的 Event 该基类不会有额外操作，默认主线程会自行处理
 *
 * @author Xavier
 * @date 2026/6/30
 */
public abstract class BaseHyggeEventListener<S, E extends BaseHyggeEvent<S>> implements ApplicationListener<E> {
    private static final Logger log = LoggerFactory.getLogger(BaseHyggeEventListener.class);
    private static final JsonHelper<ObjectMapper> jsonHelper = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(false);

    @Override
    public void onApplicationEvent(E event) {
        HyggeEventListenerContext<S, E> context = new HyggeEventListenerContext<>();
        context.setEvent(event);
        try {
            mainProcess(context, event);
        } catch (Throwable throwable) {
            ultimateThrowableHook(context, throwable);
        }
    }

    protected void mainProcess(HyggeEventListenerContext<S, E> context, E event) {
        if (event.isAsynchronous) {
            asynchronousProcess(context, event);
            return;
        }
        executeEvent(context, event);
    }

    protected void asynchronousProcess(HyggeEventListenerContext<S, E> context, E event) {
        Map<String, String> contextMapFromParent = MDC.getCopyOfContextMap();

        CompletableFuture.runAsync(() -> {
            initMDCForSubThread(contextMapFromParent);

            executeEvent(context, event);

            // 清扫子线程 MDC
            clearMDCForSubThread();
        }).exceptionally(throwable -> {
            ultimateThrowableHook(context, throwable);
            // 自动转换 Void
            return null;
        });
    }

    protected void executeEvent(HyggeEventListenerContext<S, E> context, E event) {
        try {
            stepAutoIncrease(context, event);

            context.setRowEventInfo(getEventLogInfo(context, event));

            handleEvent(context, event);
        } catch (Exception exception) {
            context.setException(exception);
            handleException(context, exception);
        } finally {
            finallyHook(context, event);
            printLog(context, context.getRowEventInfo());
        }
    }

    /**
     * 获取当前监听器名称，用于自动日志记录
     */
    protected abstract String getListenerName();

    protected void stepAutoIncrease(HyggeEventListenerContext<S, E> context, E event) {
        event.setStepCount(event.getStepCount() + 1);
    }

    protected Map<String, Object> getEventLogInfo(HyggeEventListenerContext<S, E> context, E event) {
        return event.toInfoMap();
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
     * 仅针对需要异步处理的 event 进行子线程 MDC 配置
     */
    protected void clearMDCForSubThread() {
        MDC.clear();
    }

    /**
     * 处理事件的具体方法。执行该方法前会先自动执行
     */
    protected abstract void handleEvent(HyggeEventListenerContext<S, E> context, E event);

    /**
     * 执行中发生异常的处理机制
     *
     * @param exception 运行过程中抛出的异常
     */
    protected void handleException(HyggeEventListenerContext<S, E> context, Exception exception) {
        // 默认状态下，已经在 printLog 中进行了异常打印输出，此处不再额外操作
    }

    protected void printLog(HyggeEventListenerContext<S, E> context, Map<String, Object> rowEventInfo) {
        long cost = System.currentTimeMillis() - context.getStartTs();

        rowEventInfo.put("cost", cost);

        String jsonInfo = jsonHelper.formatAsString(rowEventInfo);

        if (context.isExceptionOccurred()) {
            String logInfo = getListenerName() + " consume failure:" + jsonInfo;
            log.error(logInfo, context.getException());
        } else {
            log.info("{} consume success:{}",
                    getListenerName(),
                    jsonInfo
            );
        }
    }

    /**
     * 无论成功与否，必然会在最后执行的钩子函数，默认什么也不做。
     */
    protected void finallyHook(HyggeEventListenerContext<S, E> context, E event) {
        // 默认什么也不做，给子类提供一个钩子函数
    }

    /**
     * 最终的异常处理器，这是最后一道防线，该方法严禁抛出异常。<br/>
     * <p>
     * 默认实现是单纯打印日志。
     */
    protected void ultimateThrowableHook(HyggeEventListenerContext<S, E> context, Throwable throwable) {
        String logInfo = getListenerName() + " unexpected error.";
        log.error(logInfo, throwable);
    }
}