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

package hygge.commons.template.configuration.impl;


import hygge.commons.constant.enums.ColumnTypeEnum;
import hygge.commons.template.configuration.definition.HyggeConfiguration;
import hygge.commons.template.configuration.inner.HyggeConfigurationItem;
import hygge.commons.template.configuration.inner.HyggeConfigurationItemKeeper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Hygge 配置项(每一个 key 都只能对应唯一的值)
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class DefaultHyggeConfiguration implements HyggeConfiguration {
    /**
     * 配置项默认优先级
     */
    public static final int DEFAULT_CUSTOM_ORDER = 0;
    /**
     * 配置项容器
     */
    private final HyggeConfigurationItemKeeper propertiesKeeper;

    public DefaultHyggeConfiguration() {
        this.propertiesKeeper = new HyggeConfigurationItemKeeper();
    }

    public DefaultHyggeConfiguration(Properties properties, int order) {
        this.propertiesKeeper = new HyggeConfigurationItemKeeper(properties.size(), 1F);

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            HyggeConfigurationItem<?> newItem = new HyggeConfigurationItem<>(entry.getKey().toString(), ColumnTypeEnum.analyseColumnType(entry.getValue()), entry.getValue(), order);
            propertiesKeeper.saveValue(newItem.getKey(), newItem);
        }
    }

    public DefaultHyggeConfiguration(HyggeConfigurationItemKeeper propertiesKeeper) {
        this.propertiesKeeper = propertiesKeeper;
    }

    @Override
    public int getDefaultOrder() {
        return DEFAULT_CUSTOM_ORDER;
    }

    @Override
    public HyggeConfigurationItem<?> putItem(String key, Object value) {
        return putItem(key, value, getDefaultOrder());
    }

    @Override
    public HyggeConfigurationItem<?> putItem(String key, Object value, int order) {
        synchronized (propertiesKeeper) {
            if (propertiesKeeper.containsKey(key)) {
                HyggeConfigurationItem<?> old = propertiesKeeper.getValue(key);
                if (old.getOrder() >= order) {
                    HyggeConfigurationItem<?> newOne = new HyggeConfigurationItem<>(key, ColumnTypeEnum.analyseColumnType(value), value, order);
                    return propertiesKeeper.saveValue(key, newOne);
                }
                return old;
            } else {
                HyggeConfigurationItem<?> item = new HyggeConfigurationItem<>(key, ColumnTypeEnum.analyseColumnType(value), value, order);
                return propertiesKeeper.saveValue(key, item);
            }
        }
    }

    public boolean containsKey(String key) {
        return propertiesKeeper.containsKey(key);
    }

    @Override
    public HyggeConfigurationItem<?> getItem(String key) {
        return propertiesKeeper.getValue(key);
    }

    @Override
    public Set<String> getKeys() {
        return propertiesKeeper.getContainer().keySet();
    }

    @Override
    public Collection<HyggeConfigurationItem<?>> getItems() {
        return propertiesKeeper.getContainer().values();
    }

    @Override
    public List<HyggeConfigurationItem<?>> mergeConfiguration(HyggeConfiguration mergeTarget) {
        if (mergeTarget == null) {
            return new ArrayList<>(0);
        }
        List<HyggeConfigurationItem<?>> replaceItemList = new ArrayList<>();
        for (HyggeConfigurationItem<?> item : mergeTarget.getItems()) {
            HyggeConfigurationItem<?> oldItem = putItem(item.getKey(), item.getValue(), item.getOrder());
            if (oldItem != null) {
                replaceItemList.add(oldItem);
            }
        }
        return replaceItemList;
    }

    @Override
    public Properties toProperties() {
        Properties result = new Properties();
        for (HyggeConfigurationItem<?> item : getItems()) {
            String key = item.getKey();
            if (item.getValue() != null) {
                String value = item.getValue().toString();
                result.setProperty(key, value);
            }
        }
        return result;
    }

    public HyggeConfigurationItemKeeper getPropertiesKeeper() {
        return propertiesKeeper;
    }
}
