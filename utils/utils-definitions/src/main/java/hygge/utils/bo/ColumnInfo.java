package hygge.utils.bo;


import hygge.commons.enums.ColumnTypeEnum;
import hygge.commons.exceptions.UtilRuntimeException;
import hygge.utils.UtilsCreator;
import hygge.utils.definitions.ParameterHelper;

/**
 * DAO 操作的属性信息
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class ColumnInfo {
    private static ParameterHelper parameterHelper = UtilsCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
    /**
     * 属性名称
     */
    private String columnName;
    /**
     * 属性别名(为空则直接取 columnName)
     */
    private String columnAlias;
    /**
     * 属性类型
     */
    private ColumnTypeEnum columnTypeEnum;
    /**
     * key 是否可空 true: 可空
     */
    private boolean keyNullable;
    /**
     * value 是否可空 true: 可空
     */
    private boolean valueNullable;
    /**
     * 允许为空时，目标对象为空的默认值，不允许为空时可不传(暂未启用)
     */
    private Object defaultValue;
    /**
     * 最小值(数字类型)/字符长度(字符串)/容器大小
     */
    private Number minLength;
    /**
     * 最大值(数字类型)/字符长度(字符串)/容器大小
     */
    private Number maxLength;

    public ColumnInfo(String columnName, String columnAlias, ColumnTypeEnum columnTypeEnum, boolean keyNullable, boolean valueNullable, Number minLength, Number maxLength) {
        this.columnName = parameterHelper.stringNotEmpty("columnName", (Object) columnName);
        this.columnAlias = parameterHelper.stringOfNullable(columnAlias, columnName);
        parameterHelper.objectNotNull("columnTypeEnum", columnTypeEnum);
        this.columnTypeEnum = columnTypeEnum;
        this.keyNullable = parameterHelper.booleanFormatNotEmpty("keyNullable", keyNullable);
        this.valueNullable = parameterHelper.booleanFormatNotEmpty("valueNullable", valueNullable);
        parameterHelper.objectNotNull("minLength", minLength);
        this.minLength = minLength;
        parameterHelper.objectNotNull("maxLength", maxLength);
        this.maxLength = maxLength;
    }

    /**
     * 根据当前属性信息校验并取回目标值
     *
     * @param target 待检测对象
     * @return 属性规范值
     */
    public Object checkAndGetColumn(Object target) {
        Object result;
        if (valueNullable) {
            // 允许为空
            switch (columnTypeEnum) {
                case STRING:
                    result = parameterHelper.string(columnName, target, minLength.intValue(), maxLength.intValue());
                    break;
                case BYTE:
                    result = parameterHelper.byteFormat(columnName, target, minLength.byteValue(), maxLength.byteValue());
                    break;
                case SHORT:
                    result = parameterHelper.shortFormat(columnName, target, minLength.shortValue(), maxLength.shortValue());
                    break;
                case INTEGER:
                    result = parameterHelper.integerFormat(columnName, target, minLength.intValue(), maxLength.intValue());
                    break;
                case FLOAT:
                    result = parameterHelper.floatFormat(columnName, target, minLength.floatValue(), maxLength.floatValue());
                    break;
                case DOUBLE:
                    result = parameterHelper.doubleFormat(columnName, target, minLength.doubleValue(), maxLength.doubleValue());
                    break;
                case BOOLEAN:
                    result = parameterHelper.booleanFormat(columnName, target);
                    break;
                default:
                    throw new UtilRuntimeException(String.format("Unexpected ColumnInfo:%s,it should be STRING,BYTE,SHORT,INTEGER,FLOAT,DOUBLE,BOOLEAN.", columnTypeEnum));
            }
        } else {
            // 不允许为空
            switch (columnTypeEnum) {
                case STRING:
                    result = parameterHelper.stringNotEmpty(columnName, target, minLength.intValue(), maxLength.intValue());
                    break;
                case BYTE:
                    result = parameterHelper.byteFormatNotEmpty(columnName, target, minLength.byteValue(), maxLength.byteValue());
                    break;
                case SHORT:
                    result = parameterHelper.shortFormatNotEmpty(columnName, target, minLength.shortValue(), maxLength.shortValue());
                    break;
                case INTEGER:
                    result = parameterHelper.integerFormatNotEmpty(columnName, target, minLength.intValue(), maxLength.intValue());
                    break;
                case FLOAT:
                    result = parameterHelper.floatFormatNotEmpty(columnName, target, minLength.floatValue(), maxLength.floatValue());
                    break;
                case DOUBLE:
                    result = parameterHelper.doubleFormatNotEmpty(columnName, target, minLength.doubleValue(), maxLength.doubleValue());
                    break;
                case BOOLEAN:
                    result = parameterHelper.booleanFormatNotEmpty(columnName, target);
                    break;
                default:
                    throw new UtilRuntimeException(String.format("Unexpected ColumnInfo:%s,it should be STRING,BYTE,SHORT,INTEGER,FLOAT,DOUBLE,BOOLEAN.", columnTypeEnum));
            }
        }
        return result;
    }

    public static ParameterHelper getParameterHelper() {
        return parameterHelper;
    }

    public static void setParameterHelper(ParameterHelper parameterHelper) {
        ColumnInfo.parameterHelper = parameterHelper;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnAlias() {
        return columnAlias;
    }

    public void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    public ColumnTypeEnum getColumnTypeEnum() {
        return columnTypeEnum;
    }

    public void setColumnTypeEnum(ColumnTypeEnum columnTypeEnum) {
        this.columnTypeEnum = columnTypeEnum;
    }

    public boolean isKeyNullable() {
        return keyNullable;
    }

    public void setKeyNullable(boolean keyNullable) {
        this.keyNullable = keyNullable;
    }

    public boolean isValueNullable() {
        return valueNullable;
    }

    public void setValueNullable(boolean valueNullable) {
        this.valueNullable = valueNullable;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Number getMinLength() {
        return minLength;
    }

    public void setMinLength(Number minLength) {
        this.minLength = minLength;
    }

    public Number getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Number maxLength) {
        this.maxLength = maxLength;
    }
}