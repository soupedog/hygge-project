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

package hygge.util.definition;

/**
 * 随机唯一标识生成器
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface RandomUniqueGenerator extends HyggeUtil {
    /**
     * 此处默认指向 {@link hygge.utils.impl.SnowFlakeGenerator}
     */
    @Override
    default String getHyggeName() {
        return "SnowFlakeGenerator";
    }

    /**
     * 返回一个不重复的数字 id
     *
     * @return 不重复的数字 id
     */
    long createKey();
}
