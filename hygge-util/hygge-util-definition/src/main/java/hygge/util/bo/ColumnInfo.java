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

package hygge.util.bo;


import hygge.commons.constant.enums.ColumnTypeEnum;
import hygge.commons.exception.UtilRuntimeException;
import hygge.util.UtilCreator;
import hygge.util.definition.ParameterHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * DAO 操作的属性信息
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class ColumnInfo {
    private static ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
    /**
     * 属性名称
     */
    private String columnName;
    /**
     * 属性在数据库中的实际名称(为空则直接取 columnName)
     */
    private String columnNameInDatabase;
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
    private Number min;
    /**
     * 最大值(数字类型)/字符长度(字符串)/容器大小
     */
    private Number max;
    /**
     * 用于约束 BigDecimal，其他类型非必须
     */
    private int scale;
    /**
     * 用于约束 BigDecimal，其他类型非必须
     */
    private RoundingMode roundingMode;

    public ColumnInfo(boolean keyNullable, boolean valueNullable, String columnName, String columnNameInDatabase, Byte min, Byte max) {
        init(columnName, columnNameInDatabase, ColumnTypeEnum.BYTE, keyNullable, valueNullable, min, max);
    }

    public ColumnInfo(boolean keyNullable, boolean valueNullable, String columnName, String columnNameInDatabase, Short min, Short max) {
        init(columnName, columnNameInDatabase, ColumnTypeEnum.SHORT, keyNullable, valueNullable, min, max);
    }

    public ColumnInfo(boolean keyNullable, boolean valueNullable, String columnName, String columnNameInDatabase, Integer min, Integer max) {
        init(columnName, columnNameInDatabase, ColumnTypeEnum.INTEGER, keyNullable, valueNullable, min, max);
    }

    public ColumnInfo(boolean keyNullable, boolean valueNullable, String columnName, String columnNameInDatabase, Long min, Long max) {
        init(columnName, columnNameInDatabase, ColumnTypeEnum.LONG, keyNullable, valueNullable, min, max);
    }

    public ColumnInfo(boolean keyNullable, boolean valueNullable, String columnName, String columnNameInDatabase, Float min, Float max) {
        init(columnName, columnNameInDatabase, ColumnTypeEnum.FLOAT, keyNullable, valueNullable, min, max);
    }

    public ColumnInfo(boolean keyNullable, boolean valueNullable, String columnName, String columnNameInDatabase, Double min, Double max) {
        init(columnName, columnNameInDatabase, ColumnTypeEnum.DOUBLE, keyNullable, valueNullable, min, max);
    }

    public ColumnInfo(boolean keyNullable, boolean valueNullable, String columnName, String columnNameInDatabase, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max) {
        init(columnName, columnNameInDatabase, ColumnTypeEnum.BIG_DECIMAL, keyNullable, valueNullable, min, max);
        this.scale = scale;
        this.roundingMode = roundingMode;
    }

    /**
     * 适用于复杂对象简单验证非空性
     */
    public ColumnInfo(boolean keyNullable, boolean valueNullable, String columnName, String columnNameInDatabase) {
        init(columnName, columnNameInDatabase, ColumnTypeEnum.OTHER_OBJECT, keyNullable, valueNullable, null, null);
    }

    /**
     * 将复杂对象简单验证非空性转化成 Boolean 类型验证非空性的语法糖，常按如下方法使用
     *
     * <pre>
     *     new ColumnInfo(false,true,"testKey",null).toBooleanColumn();
     * </pre>
     */
    public ColumnInfo toBooleanColumn() {
        this.columnTypeEnum = ColumnTypeEnum.BOOLEAN;
        return this;
    }

    /**
     * 将复杂对象简单验证非空性转化成 String 类型验证非空性/字符串长度的语法糖，常按如下方法使用
     *
     * <pre>
     *     new ColumnInfo(false,true,"testKey",null).toStringColumn(1, 8);
     * </pre>
     */
    public ColumnInfo toStringColumn(int minLength, int maxLength) {
        this.columnTypeEnum = ColumnTypeEnum.STRING;
        this.min = minLength;
        this.max = maxLength;
        return this;
    }

    private void init(String columnName, String columnDatabaseName, ColumnTypeEnum columnTypeEnum, boolean keyNullable, boolean valueNullable, Number min, Number max) {
        this.columnName = parameterHelper.stringNotEmpty("columnName", (Object) columnName);
        this.columnNameInDatabase = parameterHelper.stringOfNullable(columnDatabaseName, columnName);
        parameterHelper.objectNotNull("columnTypeEnum", columnTypeEnum);
        this.columnTypeEnum = columnTypeEnum;
        this.keyNullable = parameterHelper.booleanFormatNotEmpty("keyNullable", keyNullable);
        this.valueNullable = parameterHelper.booleanFormatNotEmpty("valueNullable", valueNullable);

        switch (this.columnTypeEnum) {
            case BOOLEAN:
            case OTHER_OBJECT:
                break;
            default:
                parameterHelper.objectNotNull("min", min);
                this.min = min;
                parameterHelper.objectNotNull("max", max);
                this.max = max;
        }
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
                    result = parameterHelper.string(columnName, target, min.intValue(), max.intValue());
                    break;
                case BYTE:
                    result = parameterHelper.byteFormat(columnName, target, min.byteValue(), max.byteValue());
                    break;
                case SHORT:
                    result = parameterHelper.shortFormat(columnName, target, min.shortValue(), max.shortValue());
                    break;
                case INTEGER:
                    result = parameterHelper.integerFormat(columnName, target, min.intValue(), max.intValue());
                    break;
                case LONG:
                    result = parameterHelper.longFormat(columnName, target, min.longValue(), max.longValue());
                    break;
                case FLOAT:
                    result = parameterHelper.floatFormat(columnName, target, min.floatValue(), max.floatValue());
                    break;
                case DOUBLE:
                    result = parameterHelper.doubleFormat(columnName, target, min.doubleValue(), max.doubleValue());
                    break;
                case BIG_DECIMAL:
                    result = parameterHelper.bigDecimalFormat(columnName, target, scale, roundingMode, (BigDecimal) min, (BigDecimal) max);
                    break;
                case BOOLEAN:
                    result = parameterHelper.booleanFormat(columnName, target);
                    break;
                case OTHER_OBJECT:
                    result = target;
                    break;
                default:
                    throw new UtilRuntimeException(String.format("Unexpected ColumnInfo:%s,it should be STRING,BYTE,SHORT,INTEGER,FLOAT,DOUBLE,BOOLEAN.", columnTypeEnum));
            }
        } else {
            // 不允许为空
            switch (columnTypeEnum) {
                case STRING:
                    result = parameterHelper.stringNotEmpty(columnName, target, min.intValue(), max.intValue());
                    break;
                case BYTE:
                    result = parameterHelper.byteFormatNotEmpty(columnName, target, min.byteValue(), max.byteValue());
                    break;
                case SHORT:
                    result = parameterHelper.shortFormatNotEmpty(columnName, target, min.shortValue(), max.shortValue());
                    break;
                case INTEGER:
                    result = parameterHelper.integerFormatNotEmpty(columnName, target, min.intValue(), max.intValue());
                    break;
                case LONG:
                    result = parameterHelper.longFormatNotEmpty(columnName, target, min.longValue(), max.longValue());
                    break;
                case FLOAT:
                    result = parameterHelper.floatFormatNotEmpty(columnName, target, min.floatValue(), max.floatValue());
                    break;
                case DOUBLE:
                    result = parameterHelper.doubleFormatNotEmpty(columnName, target, min.doubleValue(), max.doubleValue());
                    break;
                case BOOLEAN:
                    result = parameterHelper.booleanFormatNotEmpty(columnName, target);
                    break;
                case BIG_DECIMAL:
                    result = parameterHelper.bigDecimalFormatNotEmpty(columnName, target, scale, roundingMode, (BigDecimal) min, (BigDecimal) max);
                    break;
                case OTHER_OBJECT:
                    parameterHelper.objectNotNull(columnName, target);
                    result = target;
                    break;
                default:
                    throw new UtilRuntimeException(String.format("Unexpected ColumnInfo:%s,it should be STRING,BYTE,SHORT,INTEGER,LONG,FLOAT,DOUBLE,BOOLEAN.", columnTypeEnum));
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

    public String getColumnNameInDatabase() {
        return columnNameInDatabase;
    }

    public void setColumnNameInDatabase(String columnNameInDatabase) {
        this.columnNameInDatabase = columnNameInDatabase;
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

    public Number getMin() {
        return min;
    }

    public void setMin(Number min) {
        this.min = min;
    }

    public Number getMax() {
        return max;
    }

    public void setMax(Number max) {
        this.max = max;
    }
}
