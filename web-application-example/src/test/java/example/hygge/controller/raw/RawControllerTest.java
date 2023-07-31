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

package example.hygge.controller.raw;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 冷启动测试结果：完成近似相同工作量，AOP 并未产生明显性能下降
 *
 * @author Xavier
 * @date 2023/3/29
 * @since 1.0
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class RawControllerTest {
    public static int times = 10000;

    @Test
    void raw(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/actuator/health")).andExpect(status().isOk());

        long startTs = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            mvc.perform(get("/raw/swaggerFriendly")).andExpect(status().isOk());
        }

        log.info("Raw 耗时:" + (System.currentTimeMillis() - startTs));
    }
}
