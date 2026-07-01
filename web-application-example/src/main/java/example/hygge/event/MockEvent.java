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

import hygge.commons.spring.event.BaseHyggeEvent;

import java.time.Clock;

/**
 * @author Xavier
 * @date 2026/7/1
 */
public class MockEvent extends BaseHyggeEvent<Integer> {
    // source 是重发 Event 计数器
    public MockEvent(Integer source) {
        super(source);
    }

    public MockEvent(Integer source, Clock clock) {
        super(source, clock);
    }
}
