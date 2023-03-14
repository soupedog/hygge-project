package hygge.commons.constants.enums;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 参数类型枚举
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public enum ColumnTypeEnum {
    /**
     * null
     */
    NULL,
    /**
     * 字符串(String)
     */
    STRING,
    /**
     * 字节型(Byte)
     */
    BYTE,
    /**
     * 短整型(Short)
     */
    SHORT,
    /**
     * 整型(Integer)
     */
    INTEGER,
    /**
     * 长整型(Long)
     */
    LONG,
    /**
     * 浮点型(Float)
     */
    FLOAT,
    /**
     * 双精度浮点型(Double)
     */
    DOUBLE,
    /**
     * 布尔型(Boolean)
     */
    BOOLEAN,
    /**
     * 数字类型统称 [Byte,Short,Integer,Long,Float,Double,BigDecimal]
     */
    NUMBER,
    /**
     * 长小数类型(BigDecimal)
     */
    BIG_DECIMAL,
    /**
     * 变长数组类型(List)
     */
    LIST,
    /**
     * 去重数组类型(Set)
     */
    SET,
    /**
     * 键值对集合类型(Map)
     */
    MAP,
    /**
     * 其他对象类型(Object)
     */
    OTHER_OBJECT,
    ;

    /**
     * 分析目标对象大致类型
     *
     * @param target 待分析的目标
     * @return 分析出的大致类型
     */
    public static ColumnTypeEnum analyseColumnType(Object target) {
        if (target == null) {
            return NULL;
        } else if (target instanceof String) {
            return STRING;
        } else if (target instanceof Integer) {
            return INTEGER;
        } else if (target instanceof Long) {
            return LONG;
        } else if (target instanceof Boolean) {
            return BOOLEAN;
        } else if (target instanceof Byte) {
            return BYTE;
        } else if (target instanceof List) {
            return LIST;
        } else if (target instanceof Map) {
            return MAP;
        } else if (target instanceof Set) {
            return SET;
        } else if (target instanceof BigDecimal) {
            return BIG_DECIMAL;
        } else if (target instanceof Short) {
            return SHORT;
        } else if (target instanceof Float) {
            return FLOAT;
        } else if (target instanceof Double) {
            return DOUBLE;
        } else {
            return OTHER_OBJECT;
        }
    }

    /**
     * 判断目标是否属于某种类型
     */
    public static boolean isTypeOf(Object target, ColumnTypeEnum columnType) {
        ColumnTypeEnum type = analyseColumnType(target);
        if (columnType == NUMBER) {
            switch (type) {
                case BYTE:
                case SHORT:
                case INTEGER:
                case LONG:
                case FLOAT:
                case DOUBLE:
                case BIG_DECIMAL:
                    return true;
                default:
                    return false;
            }
        } else {
            return type.equals(columnType);
        }
    }
}
