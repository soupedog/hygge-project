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

package hygge.util.bo.configuration.arranger;

import hygge.util.bo.configuration.arranger.analyst.PropertiesPrimaryAnalyst;
import hygge.util.bo.configuration.arranger.analyst.PropertiesSpringAnalyst;
import hygge.util.definition.analyst.ConfigurationTypeAnalyst;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 配置项整理工具配置项
 *
 * @author Xavier
 * @date 2024/9/25
 * @since 1.0
 */
public class PropertiesArrangerConfiguration {
    /**
     * 需跳过无需整理的配置项文件名称
     */
    private List<String> skipFileNameList;
    /**
     * 重要配置项 key 名称
     */
    private List<String> primaryKey;

    /**
     * 类型分析器
     */
    private List<ConfigurationTypeAnalyst> analystList;

    public PropertiesArrangerConfiguration() {
        this.skipFileNameList = new ArrayList<>(0);
        this.primaryKey = new ArrayList<>(0);
        this.analystList = new ArrayList<>(2);
        // 添加默认键值类型分析器
        analystList.add(new PropertiesSpringAnalyst());
        sortAnalystList();
    }

    public PropertiesArrangerConfiguration(List<String> primaryKey) {
        this.skipFileNameList = new ArrayList<>(0);
        this.primaryKey = primaryKey;
        this.analystList = new ArrayList<>(2);
        // 添加两个默认键值类型分析器
        analystList.add(new PropertiesPrimaryAnalyst());
        analystList.add(new PropertiesSpringAnalyst());
        sortAnalystList();
    }

    public PropertiesArrangerConfiguration(List<String> skipFileNameList, List<String> primaryKey, List<ConfigurationTypeAnalyst> analystList) {
        this.skipFileNameList = skipFileNameList;
        this.primaryKey = primaryKey;
        this.analystList = analystList;
        sortAnalystList();
    }

    public void sortAnalystList() {
        if (analystList != null) {
            analystList.sort(Comparator.comparingInt(configurationTypeAnalyst -> configurationTypeAnalyst.typeResult().getOrder()));
        }
    }

    public List<String> getSkipFileNameList() {
        return skipFileNameList;
    }

    public void setSkipFileNameList(List<String> skipFileNameList) {
        this.skipFileNameList = skipFileNameList;
    }

    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<ConfigurationTypeAnalyst> getAnalystList() {
        return analystList;
    }

    public void setAnalystList(List<ConfigurationTypeAnalyst> analystList) {
        this.analystList = analystList;
    }
}
