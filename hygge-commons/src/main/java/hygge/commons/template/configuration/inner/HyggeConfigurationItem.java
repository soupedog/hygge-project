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

package hygge.commons.template.configuration.inner;


import hygge.commons.constant.enums.ColumnTypeEnum;

/**
 * Hygge 配置项键值实体
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class HyggeConfigurationItem<T> {
    /**
     * 键
     */
    private String key;
    /**
     * 值类型
     */
    private ColumnTypeEnum valueType;
    /**
     * 值
     */
    private T value;
    /**
     * 优先级,越小优先级越高
     */
    private int order;

    public HyggeConfigurationItem() {
    }

    public HyggeConfigurationItem(String key, ColumnTypeEnum valueType, T value, int order) {
        this.key = key;
        this.valueType = valueType;
        this.value = value;
        this.order = order;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ColumnTypeEnum getValueType() {
        return valueType;
    }

    public void setValueType(ColumnTypeEnum valueType) {
        this.valueType = valueType;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HyggeConfigurationItem<?> that = (HyggeConfigurationItem<?>) o;

        if (!key.equals(that.key)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
