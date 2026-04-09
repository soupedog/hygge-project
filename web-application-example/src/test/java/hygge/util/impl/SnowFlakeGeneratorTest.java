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


import hygge.util.UtilCreator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class SnowFlakeGeneratorTest {
    @Test
    void createId() {
        SnowFlakeGenerator snowFlakeGenerator = UtilCreator.INSTANCE.getDefaultInstance(SnowFlakeGenerator.class);
        HashMap<Long, Boolean> map = new HashMap<>(7000000);
        int count = 0;

        long ts = System.currentTimeMillis() + 5000L;
        while (System.currentTimeMillis() < ts) {
            map.put(snowFlakeGenerator.createKey(), Boolean.TRUE);
            count++;
        }

        System.out.println(snowFlakeGenerator);
        System.out.println(String.format(" 5 秒共生成 %d 个 id ，无重复：%s ", map.size(), count == map.size()));
    }
}