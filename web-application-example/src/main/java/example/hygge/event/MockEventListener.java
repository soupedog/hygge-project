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

package example.hygge.event;

import example.hygge.service.HyggeEventService;
import hygge.commons.spring.event.BaseHyggeEventListener;
import hygge.commons.spring.event.HyggeEventListenerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static example.hygge.event.MockEventListenerKey.SOME_INFO;
import static example.hygge.event.MockEventListenerKey.SOME_INFO_2;

/**
 * @author Xavier
 * @date 2026/7/1
 */
@Service
public class MockEventListener extends BaseHyggeEventListener<Integer, MockEvent> {
    private static final Logger log = LoggerFactory.getLogger(MockEventListener.class);
    private static final String NAME = MockEventListener.class.getSimpleName();
    private final HyggeEventService hyggeEventService;

    public MockEventListener(HyggeEventService hyggeEventService) {
        this.hyggeEventService = hyggeEventService;
    }

    @Override
    protected String getListenerName() {
        return NAME;
    }

    @Override
    protected void handleEvent(HyggeEventListenerContext<Integer, MockEvent> context, MockEvent event) {
        context.saveObject(SOME_INFO, "测试值");
        context.saveObject(SOME_INFO_2, new ArrayList<>());
        // 模拟业务逻辑处理耗时
        try {
            Thread.sleep(ThreadLocalRandom.current().nextLong(100L));
        } catch (InterruptedException e) {
            log.error("异常", e);
        }

        if (event.getActualSource() > 1) {
            // 循环发起 event 模拟 event 执行链
            boolean asynchronous = ThreadLocalRandom.current().nextInt(10) > 7;

            MockEvent nextEvent = hyggeEventService.buildEvent(event.getActualSource() - 1, MockEvent::new)
                    .asynchronous(asynchronous)
                    .build();

            nextEvent.setTrigger(event);

            hyggeEventService.fireEvent(nextEvent);
        }

        if (ThreadLocalRandom.current().nextInt(10) > 8) {
            throw new RuntimeException(getListenerName() + " 模拟的随机异常：" + ThreadLocalRandom.current().nextInt(6666));
        }
    }

    @Override
    protected void printLog(HyggeEventListenerContext<Integer, MockEvent> context, Map<String, Object> rowEventInfo) {
        super.printLog(context, rowEventInfo);
        log.info("演示 context 传递参数 SomeInfo:{} SomeInfo2:{}",
                context.getObject(SOME_INFO),
                context.getObject(SOME_INFO_2));

    }
}
