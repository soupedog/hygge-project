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

package hygge.util.impl;


import hygge.util.base.BaseTimeHelper;

import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * 默认的 TimeHelper 实现类<br/>
 * 指定默认时区为 Asia/Shanghai 东八区
 *
 * @author Xavier
 * @date 2022/6/26
 * @since 1.0
 */
public class DefaultTimeHelper extends BaseTimeHelper {
    @Override
    public void initZoneOffset() {
        // 默认为应用部署时区
        TimeZone timeZone = TimeZone.getDefault();
        // 毫秒转秒，精度下降到秒级别
        int offsetSecond = timeZone.getRawOffset() / 1000;
        defaultZoneOffset = ZoneOffset.ofTotalSeconds(offsetSecond);
    }
}
