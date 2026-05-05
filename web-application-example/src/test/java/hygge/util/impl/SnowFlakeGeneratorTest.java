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

package hygge.util.impl;/*
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


import hygge.commons.constant.enums.DateTimeFormatModeEnum;
import hygge.util.UtilCreator;
import hygge.util.definition.TimeHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

@Slf4j
class SnowFlakeGeneratorTest {
    @Test
    void createId() {
        SnowFlakeGenerator snowFlakeGenerator = UtilCreator.INSTANCE.getDefaultInstance(SnowFlakeGenerator.class);
        HashMap<Long, Boolean> map = new HashMap<>(8000000);
        int count = 0;

        long ts = System.currentTimeMillis() + 5000L;
        while (System.currentTimeMillis() < ts) {
            map.put(snowFlakeGenerator.createKey(), Boolean.TRUE);
            count++;
        }
        log.info(snowFlakeGenerator.toString());
        log.info(String.format(" 5 秒共生成 %d 个 id ，无重复：%s ", map.size(), count == map.size()));
    }

    @Test
    void analyze() {
        SnowFlakeGenerator snowFlakeGenerator = new SnowFlakeGenerator(0L, 5, 4, 12);

        TimeHelper timeHelper = UtilCreator.INSTANCE.getDefaultInstance(TimeHelper.class);
        long id = snowFlakeGenerator.createKey();
        // 稍微延迟一下，避免序列号是 0
        id = snowFlakeGenerator.createKey();

        log.info("解析回的自增序列：" + snowFlakeGenerator.getSequenceFromId(id));
        log.info("解析回的生成器编号：" + snowFlakeGenerator.getWorkerIdFromId(id));
        log.info(timeHelper.format(snowFlakeGenerator.getRealTimestampFromId(id), DateTimeFormatModeEnum.DEFAULT));

        log.info("原始 ID 值：" + id);
        IdObfuscator idObfuscator = new IdObfuscator();
        String encrypted = idObfuscator.obfuscate(id);
        log.info("混淆后 ID 值：" + encrypted);
        log.info("解析后 ID 值：" + idObfuscator.reveal(encrypted));
    }
}