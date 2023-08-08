/*
 * Copyright 2022-2023 the original author or authors.
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

package example.hygge.config;

import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 * 该类无实际含义，仅用于说明各 Hygge 组件在 Spring Boot 的哪个环节进行初始化
 *
 * @author Xavier
 * @date 2023/8/8
 * @since 1.0
 */
public class SpringEventPrintListener implements Ordered, ApplicationListener<SpringApplicationEvent> {
    @Override
    public int getOrder() {
        // 指定该类中 print 方法位于当前 Event 的即将结束阶段
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void onApplicationEvent(SpringApplicationEvent event) {
        System.out.println("↑ —————————————————————————————— " + event.getClass().getSimpleName() + " —————————————————————————————— ↑");
    }
}
