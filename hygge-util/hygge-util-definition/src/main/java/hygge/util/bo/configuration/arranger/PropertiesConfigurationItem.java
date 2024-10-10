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

import hygge.util.definition.analyst.ConfigurationType;
import hygge.util.definition.analyst.ConfigurationTypeAnalyst;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置项
 *
 * @author Xavier
 * @date 2024/9/25
 * @since 1.0
 */
public class PropertiesConfigurationItem {
    private String key;
    private String value;
    /**
     * 配置项注释，列表中一个元素代表一行
     */
    private List<String> commentList = new ArrayList<>();
    /**
     * 默认一个最低优先级
     */
    private ConfigurationType type = PropertiesDefaultConfigurationType.DEFAULT;

    public void initType(PropertiesArrangerConfiguration configuration) {
        List<ConfigurationTypeAnalyst> analystList = configuration.getAnalystList();

        for (ConfigurationTypeAnalyst analyst : analystList) {
            ConfigurationType currentType = analyst.typeResult();

            if (this.type.getOrder() < currentType.getOrder()) {
                continue;
            }
            if (analyst.isMatched(configuration, this.key)) {
                this.type = currentType;
            }
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<String> commentList) {
        this.commentList = commentList;
    }

    public ConfigurationType getType() {
        return type;
    }

    public void setType(ConfigurationType type) {
        this.type = type;
    }
}
