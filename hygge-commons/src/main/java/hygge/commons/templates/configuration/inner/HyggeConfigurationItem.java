package hygge.commons.templates.configuration.inner;


import hygge.commons.constants.enums.ColumnTypeEnum;

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
