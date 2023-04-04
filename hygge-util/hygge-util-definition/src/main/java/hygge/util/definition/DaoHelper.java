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


import hygge.util.bo.ColumnInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * DAO 工具类
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface DaoHelper extends HyggeUtil {
    /**
     * 根据 Map 容器中的属性信息，筛选出有效属性并生成新的 Map 结构(用于非全量更新)
     *
     * @param rawData             原始数据
     * @param checkInfoCollection 属性信息
     * @return 筛选过后的新 Map,不会为 null
     */
    HashMap<String, Object> filterOutTheFinalColumns(Map<String, Object> rawData, Collection<ColumnInfo> checkInfoCollection);

    /**
     * 根据 Map 容器中的属性信息，筛选出有效属性并生成新的 Map 结构(用于非全量更新)
     *
     * @param rawData             原始数据
     * @param checkInfoCollection 属性信息
     * @param successCallBack     成功筛选出有效属性时的回调函数
     * @return 筛选过后的新 Map,不会为 null
     */
    HashMap<String, Object> filterOutTheFinalColumns(Map<String, Object> rawData, Collection<ColumnInfo> checkInfoCollection, UnaryOperator<HashMap<String, Object>> successCallBack);
}
