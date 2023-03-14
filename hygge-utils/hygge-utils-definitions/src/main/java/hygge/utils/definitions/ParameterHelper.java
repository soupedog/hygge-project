package hygge.utils.definitions;

import hygge.commons.constant.enums.StringFormatModeEnum;
import hygge.commons.exception.ParameterRuntimeException;
import hygge.commons.template.definitions.InfoMessageSupplier;
import hygge.utils.HyggeUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;

/**
 * 参数校验工具类
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
@SuppressWarnings("java:S2447")
public interface ParameterHelper extends HyggeUtil, InfoMessageSupplier {
    /**
     * 首字母大写常规模式，入参字符串最小长度
     */
    int UPPER_CASE_FIRST_LETTER_MIN_LENGTH = 2;
    /**
     * 首字母小写常规模式，入参字符串最小长度
     */
    int LOWER_CASE_FIRST_LETTER_MIN_LENGTH = 2;

    /**
     * 默认的异常处理函数
     *
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @param throwable    触发非预期事件的异常(可能为空)
     * @throws ParameterRuntimeException 参数非预期异常
     */
    default void hookUnexpectedEvent(String errorMessage, Throwable throwable) {
        throw new ParameterRuntimeException(errorMessage, throwable);
    }

    /**
     * 验证目标不可为 null<br/>
     * 目标对象为 null 则触发非预期事件
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     */
    default void objectNotNull(String targetName, Object target) {
        objectNotNull(target, "Unexpected " + targetName + ", it can't be null.");
    }

    /**
     * 验证目标不可为 null<br/>
     * 目标对象为 null 则触发非预期事件
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     */
    default void objectNotNull(Object target, String errorMessage) {
        if (target == null) {
            hookUnexpectedEvent(errorMessage, null);
        }
    }

    /**
     * 转化目标对象，如果目标对象为 null 则返回 defaultValue，否则返回目标对象本身
     * <p/>
     * 注意：defaultValue 会作为 target 参数类型校验依据，请不要随意为 defaultValue 赋值为 null
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param defaultValue 入参为 null 时返回的默认值
     * @return 转化后的目标对象
     */
    @SuppressWarnings("unchecked")
    default <T> T parseObjectOfNullable(String targetName, Object target, T defaultValue) {
        T result = null;
        if (target == null) {
            result = defaultValue;
        } else {
            // 此时涉及到数据类型校验，预期值不能为空
            objectNotNull("defaultValue", defaultValue);

            if (defaultValue.getClass().isAssignableFrom(target.getClass())) {
                result = (T) target;
            } else {
                hookUnexpectedEvent(unexpectedClass(targetName, target.getClass(), defaultValue.getClass()), null);
            }
        }
        return result;
    }

    /**
     * 目标对象是否为空<br/>
     * <p/>
     * 下列内容被视为空：<br/>
     * null<br/>
     * {@link String#trim()} 操作之后 {@link String#length()} 为 0 的字符串<br/>
     * {@link Collection#size()} 为 0 的 Collection<br/>
     * {@link Map#size()} 为 0 的 Map
     *
     * @param target 被验证的目标
     * @return 目标对象是否为空
     */
    default boolean isEmpty(Object target) {
        boolean result = true;

        if (target != null) {
            if (target instanceof String) {
                result = target.toString().trim().isEmpty();
            } else if (target instanceof Collection) {
                result = ((Collection<?>) target).isEmpty();
            } else if (target instanceof Map) {
                result = ((Map<?, ?>) target).isEmpty();
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * 目标是否为非空<br/>
     * {@link ParameterHelper#isEmpty(Object)} 取反
     *
     * @param target 被验证的目标
     * @return 目标是否为非空
     */
    default boolean isNotEmpty(Object target) {
        return !isEmpty(target);
    }

    /**
     * 目标对象是否全部不为空<br/>
     * 是否每一个对象都满足 {@linkplain ParameterHelper#isNotEmpty(Object)} == true
     *
     * @param targetArray 被验证的目标
     * @return 目标对象是否全部不为空
     */
    default boolean isAllNotEmpty(Object... targetArray) {
        for (Object item : targetArray) {
            if (isEmpty(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 目标对象是否全部为空<br/>
     * 是否每一个对象都满足 {@link ParameterHelper#isEmpty(Object)} == true
     *
     * @param targetArray 被验证的目标
     * @return 目标对象是否全部为空
     */
    default boolean isAllEmpty(Object... targetArray) {
        for (Object item : targetArray) {
            if (isNotEmpty(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 目标对象是否至少一个为空<br/>
     * {@link ParameterHelper#isAllNotEmpty(Object... targetArray)} 取反
     *
     * @param targetArray 被验证的目标
     * @return 目标对象是否至少一个为空
     */
    default boolean atLeastOneEmpty(Object... targetArray) {
        return !isAllNotEmpty(targetArray);
    }

    /**
     * 目标对象是否至少一个不为空<br/>
     * {@link ParameterHelper#isAllEmpty(Object... targetArray)} 取反
     *
     * @param targetArray 被验证的目标
     * @return 目标对象是否至少一个不为空
     */
    default boolean atLeastOneNotEmpty(Object... targetArray) {
        return !isAllEmpty(targetArray);
    }

    /**
     * 验证目标不可为空<br/>
     * {@link ParameterHelper#isEmpty(Object)} == true 则触发非预期事件
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     被验证的目标
     */
    default void notEmpty(String targetName, Object target) {
        notEmpty(target, "Unexpected " + targetName + ", it can't be empty.");
    }

    /**
     * 验证目标不可为空<br/>
     * {@link ParameterHelper#isEmpty(Object)} == true 则触发非预期事件
     *
     * @param target       被验证的目标
     * @param errorMessage 不符合预期时的完整异常提示信息
     */
    default void notEmpty(Object target, String errorMessage) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
    }

    /**
     * String 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    default String hookString(String resultTemp) {
        return resultTemp;
    }

    /**
     * Byte 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    default Byte hookByte(Byte resultTemp) {
        return resultTemp;
    }

    /**
     * Short 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    default Short hookShort(Short resultTemp) {
        return resultTemp;
    }

    /**
     * Integer 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    default Integer hookInteger(Integer resultTemp) {
        return resultTemp;
    }

    /**
     * Long 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    default Long hookLong(Long resultTemp) {
        return resultTemp;
    }

    /**
     * Float 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    default Float hookFloat(Float resultTemp) {
        return resultTemp;
    }

    /**
     * Double 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    default Double hookDouble(Double resultTemp) {
        return resultTemp;
    }

    /**
     * BigDecimal 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    default BigDecimal hookBigDecimal(BigDecimal resultTemp) {
        return resultTemp;
    }

    /**
     * Boolean 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    default Boolean hookBoolean(Boolean resultTemp) {
        return resultTemp;
    }

    /**
     * 转化目标为 String(允许为空)
     *
     * @param target 目标对象
     * @return 转化成 String 的结果
     */
    String string(Object target);

    /**
     * 转化目标为 String<br/>
     * 如果目标 {@link ParameterHelper#isEmpty(Object)} == true，则使用默认值替换，否则返回目标对象本身
     *
     * @param target       目标对象
     * @param defaultValue 默认值
     * @return 转化成 String 的结果
     */
    String stringOfNullable(Object target, String defaultValue);

    /**
     * 转化目标为 String(允许为空)，仅当目标不为空时，对字符串长度进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param minLength  字符串最小长度
     * @param maxLength  字符串最大长度
     * @return 转化成 String 的结果
     */
    String string(String targetName, Object target, int minLength, int maxLength);

    /**
     * 转化目标为 String(允许为空)，仅当目标不为空是，对字符串长度进行限制(闭区间)
     *
     * @param target       目标对象
     * @param minLength    字符串最小长度
     * @param maxLength    字符串最大长度
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 String 的结果
     */
    String string(Object target, int minLength, int maxLength, String errorMessage);

    /**
     * 转化目标为 String(不许为空)
     *
     * @param target     目标对象
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @return 转化成 String 的结果
     */
    String stringNotEmpty(String targetName, Object target);

    /**
     * 转化目标为 String(不许为空)
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 String 的结果
     */
    String stringNotEmpty(Object target, String errorMessage);

    /**
     * 转化目标为 String(不许为空)，并对字符串长度进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param minLength  最小字符串长度
     * @param maxLength  最大字符串长度
     * @return 转化成 String 的结果
     */
    String stringNotEmpty(String targetName, Object target, int minLength, int maxLength);

    /**
     * 转化目标为 String(不许为空)，并对字符串长度进行限制(闭区间)
     *
     * @param target       目标对象
     * @param minLength    最小字符串长度
     * @param maxLength    最大字符串长度
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 String 的结果
     */
    String stringNotEmpty(Object target, int minLength, int maxLength, String errorMessage);

    /**
     * 转化目标为 Byte(允许为空)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @return 转化成 Byte 的结果
     */
    Byte byteFormat(String targetName, Object target);

    /**
     * 转化目标为 Byte(允许为空)
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param defaultValue 默认值。目标对象转化值满足 {@link ParameterHelper#isEmpty(Object)} == true 时，用该值进行替换。
     * @return 转化成 Byte 的结果
     */
    byte byteFormatOfNullable(String targetName, Object target, byte defaultValue);

    /**
     * 转化目标为 Byte(允许为空)
     *
     * @param target       目标对象
     * @param defaultValue 默认值。目标对象转化值满足 {@link ParameterHelper#isEmpty(Object)} == true 时，用该值进行替换。
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Byte 的结果
     */
    byte byteFormatOfNullable(Object target, byte defaultValue, String errorMessage);

    /**
     * 转化目标为 Byte(允许为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Byte 的结果
     */
    Byte byteFormat(String targetName, Object target, byte min, byte max);

    /**
     * 转化目标为 Byte(允许为空)，且对其值进行限制(闭区间)
     *
     * @param target       目标对象
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Byte 的结果
     */
    Byte byteFormat(Object target, byte min, byte max, String errorMessage);

    /**
     * 转化目标为 Byte(不许为空)
     *
     * @param target     目标对象
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @return 转化成 Byte 的结果
     */
    Byte byteFormatNotEmpty(String targetName, Object target);

    /**
     * 转化目标为 Byte(不许为空)
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Byte 的结果
     */
    Byte byteFormatNotEmpty(Object target, String errorMessage);

    /**
     * 转化目标为 Byte(不许为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Byte 的结果
     */
    Byte byteFormatNotEmpty(String targetName, Object target, byte min, byte max);

    /**
     * 转化目标为 Byte(不许为空)，且对其值进行限制(闭区间)
     *
     * @param target       目标对象
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Byte 的结果
     */
    Byte byteFormatNotEmpty(Object target, byte min, byte max, String errorMessage);

    /**
     * 转化目标为 Short(允许为空)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @return 转化成 Short 的结果
     */
    Short shortFormat(String targetName, Object target);

    /**
     * 转化目标为 Short(允许为空)
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param defaultValue 默认值。目标对象转化值满足 {@link ParameterHelper#isEmpty(Object)} == true 时，用该值进行替换。
     * @return 转化成 Short 的结果
     */
    short shortFormatOfNullable(String targetName, Object target, short defaultValue);

    /**
     * 转化目标为 Short(允许为空)
     *
     * @param target       目标对象
     * @param defaultValue 默认值。目标对象转化值满足 {@link ParameterHelper#isEmpty(Object)} == true 时，用该值进行替换。
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Short 的结果
     */
    short shortFormat(Object target, short defaultValue, String errorMessage);

    /**
     * 转化目标为 Short(允许为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Short 的结果
     */
    Short shortFormat(String targetName, Object target, short min, short max);

    /**
     * 转化目标为 Short(允许为空)，且对其值进行限制(闭区间)
     *
     * @param target       目标对象
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Short 的结果
     */
    Short shortFormat(Object target, short min, short max, String errorMessage);

    /**
     * 转化目标为 Short(不许为空)
     *
     * @param target     目标对象
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @return 转化成 Short 的结果
     */
    Short shortFormatNotEmpty(String targetName, Object target);

    /**
     * 转化目标为 Short(不许为空)
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Short 的结果
     */
    Short shortFormatNotEmpty(Object target, String errorMessage);

    /**
     * 转化目标为 Short(不许为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Short 的结果
     */
    Short shortFormatNotEmpty(String targetName, Object target, short min, short max);

    /**
     * 转化目标为 Short(不许为空)，且对其值进行限制(闭区间)
     *
     * @param target       目标对象
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Short 的结果
     */
    Short shortFormatNotEmpty(Object target, short min, short max, String errorMessage);

    /**
     * 转化目标为 Integer(允许为空)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @return 转化成 Integer 的结果
     */
    Integer integerFormat(String targetName, Object target);

    /**
     * 转化目标为 Integer(允许为空)
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param defaultValue 默认值。目标对象转化值满足 {@link ParameterHelper#isEmpty(Object)} == true 时，用该值进行替换。
     * @return 转化成 Integer 的结果
     */
    int integerFormatOfNullable(String targetName, Object target, int defaultValue);

    /**
     * 转化目标为 Integer(允许为空)
     *
     * @param target       目标对象
     * @param defaultValue 默认值。目标对象转化值满足 {@link ParameterHelper#isEmpty(Object)} == true 时，用该值进行替换。
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Integer 的结果
     */
    int integerFormat(Object target, int defaultValue, String errorMessage);

    /**
     * 转化目标为 Integer(允许为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Integer 的结果
     */
    Integer integerFormat(String targetName, Object target, int min, int max);

    /**
     * 转化目标为 Integer(允许为空)，且对其值进行限制(闭区间)
     *
     * @param target       目标对象
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Integer 的结果
     */
    Integer integerFormat(Object target, int min, int max, String errorMessage);

    /**
     * 转化目标为 Integer(不许为空)
     *
     * @param target     目标对象
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @return 转化成 Integer 的结果
     */
    Integer integerFormatNotEmpty(String targetName, Object target);

    /**
     * 转化目标为 Integer(不许为空)
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Integer 的结果
     */
    Integer integerFormatNotEmpty(Object target, String errorMessage);

    /**
     * 转化目标为 Integer(不许为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Integer 的结果
     */
    Integer integerFormatNotEmpty(String targetName, Object target, int min, int max);

    /**
     * 转化目标为 Integer(不许为空)，且对其值进行限制(闭区间)
     *
     * @param target       目标对象
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Integer 的结果
     */
    Integer integerFormatNotEmpty(Object target, int min, int max, String errorMessage);

    /**
     * 转化目标为 Long(允许为空)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @return 转化成 Long 的结果
     */
    Long longFormat(String targetName, Object target);

    /**
     * 转化目标为 Long(允许为空)
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param defaultValue 默认值。目标对象转化值满足 {@link ParameterHelper#isEmpty(Object)} == true 时，用该值进行替换。
     * @return 转化成 Long 的结果
     */
    long longFormatOfNullable(String targetName, Object target, long defaultValue);

    /**
     * 转化目标为 Long(允许为空)
     *
     * @param target       目标对象
     * @param defaultValue 默认值。目标对象转化值满足 {@link ParameterHelper#isEmpty(Object)} == true 时，用该值进行替换。
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Long 的结果
     */
    long longFormat(Object target, long defaultValue, String errorMessage);

    /**
     * 转化目标为 Long(允许为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Long 的结果
     */
    Long longFormat(String targetName, Object target, long min, long max);

    /**
     * 转化目标为 Long(允许为空)，且对其值进行限制(闭区间)
     *
     * @param target       目标对象
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Long 的结果
     */
    Long longFormat(Object target, long min, long max, String errorMessage);

    /**
     * 转化目标为 Long(不许为空)
     *
     * @param target     目标对象
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @return 转化成 Long 的结果
     */
    Long longFormatNotEmpty(String targetName, Object target);

    /**
     * 转化目标为 Long(不许为空)
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Long 的结果
     */
    Long longFormatNotEmpty(Object target, String errorMessage);

    /**
     * 转化目标为 Long(不许为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Long 的结果
     */
    Long longFormatNotEmpty(String targetName, Object target, long min, long max);

    /**
     * 转化目标为 Long(不许为空)，且对其值进行限制(闭区间)
     *
     * @param target       目标对象
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Long 的结果
     */
    Long longFormatNotEmpty(Object target, long min, long max, String errorMessage);

    /**
     * 转化目标为 Float(允许为空)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @return 转化成 Float 的结果
     */
    Float floatFormat(String targetName, Object target);

    /**
     * 转化目标为 Float(允许为空)
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param defaultValue 默认值。目标对象转化值满足 {@link ParameterHelper#isEmpty(Object)} == true 时，用该值进行替换。
     * @return 转化成 Float 的结果
     */
    float floatFormatOfNullable(String targetName, Object target, float defaultValue);

    /**
     * 转化目标为 Float(允许为空)
     *
     * @param target       目标对象
     * @param defaultValue 默认值。目标对象转化值满足 {@link ParameterHelper#isEmpty(Object)} == true 时，用该值进行替换。
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Float 的结果
     */
    float floatFormat(Object target, float defaultValue, String errorMessage);

    /**
     * 转化目标为 Float(允许为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Float 的结果
     */
    Float floatFormat(String targetName, Object target, float min, float max);

    /**
     * 转化目标为 Float(允许为空)，且对其值进行限制(闭区间)
     *
     * @param target       目标对象
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Float 的结果
     */
    Float floatFormat(Object target, float min, float max, String errorMessage);

    /**
     * 转化目标为 Float(不许为空)
     *
     * @param target     目标对象
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @return 转化成 Float 的结果
     */
    Float floatFormatNotEmpty(String targetName, Object target);

    /**
     * 转化目标为 Float(不许为空)
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Float 的结果
     */
    Float floatFormatNotEmpty(Object target, String errorMessage);

    /**
     * 转化目标为 Float(不许为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Float 的结果
     */
    Float floatFormatNotEmpty(String targetName, Object target, float min, float max);

    /**
     * 转化目标为 Float(不许为空)，且对其值进行限制(闭区间)
     *
     * @param target       目标对象
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Float 的结果
     */
    Float floatFormatNotEmpty(Object target, float min, float max, String errorMessage);

    /**
     * 转化目标为 Double(允许为空)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @return 转化成 Double 的结果
     */
    Double doubleFormat(String targetName, Object target);

    /**
     * 转化目标为 Double(允许为空)
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param defaultValue 默认值。目标对象转化值满足 {@link ParameterHelper#isEmpty(Object)} == true 时，用该值进行替换。
     * @return 转化成 Double 的结果
     */
    double doubleFormatOfNullable(String targetName, Object target, double defaultValue);

    /**
     * 转化目标为 Double(允许为空)
     *
     * @param target       目标对象
     * @param defaultValue 默认值。目标对象转化值满足 {@link ParameterHelper#isEmpty(Object)} == true 时，用该值进行替换。
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Double 的结果
     */
    double doubleFormatOfNullable(Object target, double defaultValue, String errorMessage);

    /**
     * 转化目标为 Double(允许为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Double 的结果
     */
    Double doubleFormat(String targetName, Object target, double min, double max);

    /**
     * 转化目标为 Double(允许为空)，且对其值进行限制(闭区间)
     *
     * @param target       目标对象
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Double 的结果
     */
    Double doubleFormat(Object target, double min, double max, String errorMessage);

    /**
     * 转化目标为 Double(不许为空)
     *
     * @param target     目标对象
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @return 转化成 Double 的结果
     */
    Double doubleFormatNotEmpty(String targetName, Object target);

    /**
     * 转化目标为 Double(不许为空)
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Double 的结果
     */
    Double doubleFormatNotEmpty(Object target, String errorMessage);

    /**
     * 转化目标为 Double(不许为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Double 的结果
     */
    Double doubleFormatNotEmpty(String targetName, Object target, double min, double max);

    /**
     * 转化目标为 Double(不许为空)，且对其值进行限制(闭区间)
     *
     * @param target       目标对象
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Double 的结果
     */
    Double doubleFormatNotEmpty(Object target, double min, double max, String errorMessage);

    /**
     * 转化目标为 BigDecimal(允许为空)
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @return 转化成 BigDecimal 的结果
     */
    BigDecimal bigDecimalFormat(String targetName, String target, int scale, RoundingMode roundingMode);

    /**
     * 转化目标为 BigDecimal(允许为空)
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param defaultValue 默认值
     * @return 转化成 BigDecimal 的结果
     */
    BigDecimal bigDecimalFormatOfNullable(String targetName, String target, int scale, RoundingMode roundingMode, BigDecimal defaultValue);

    /**
     * 转化目标为 BigDecimal(允许为空)
     *
     * @param target       目标对象
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param defaultValue 默认值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 BigDecimal 的结果
     */
    BigDecimal bigDecimalFormatOfNullable(String target, int scale, RoundingMode roundingMode, BigDecimal defaultValue, String errorMessage);


    /**
     * 转化目标为 BigDecimal(允许为空)
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param min          最小值
     * @param max          最大值
     * @return 转化成 BigDecimal 的结果
     */
    BigDecimal bigDecimalFormat(String targetName, String target, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max);

    /**
     * 转化目标为 BigDecimal(允许为空)
     *
     * @param target       目标对象
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 BigDecimal 的结果
     */
    BigDecimal bigDecimalFormat(String target, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max, String errorMessage);


    /**
     * 转化目标为 BigDecimal(不许为空)
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @return 转化成 BigDecimal 的结果
     */
    BigDecimal bigDecimalFormatNotEmpty(String targetName, String target, int scale, RoundingMode roundingMode);

    /**
     * 转化目标为 BigDecimal(不许为空)
     *
     * @param target       目标对象
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 BigDecimal 的结果
     */
    BigDecimal bigDecimalFormatNotEmpty(String target, int scale, RoundingMode roundingMode, String errorMessage);


    /**
     * 转化目标为 BigDecimal(不许为空)
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param min          最小值
     * @param max          最大值
     * @return 转化成 BigDecimal 的结果
     */
    BigDecimal bigDecimalFormatNotEmpty(String targetName, String target, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max);


    /**
     * 转化目标为 BigDecimal(不许为空)
     *
     * @param target       目标对象
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 BigDecimal 的结果
     */
    BigDecimal bigDecimalFormatNotEmpty(String target, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max, String errorMessage);

    /**
     * 默认的 Byte 转化函数<br/>
     * PS:目标对象为 null 或空字符串将转化为 null
     *
     * @param targetName 目标对象名称
     * @param target     目标对象
     * @return 转化后的数据
     */
    default Byte parseByte(String targetName, Object target) {
        return parseByte(target, unexpectedObject("value", target, targetName, Byte.class.getSimpleName()));
    }

    /**
     * 默认的 Byte 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化后的数据
     */
    default Byte parseByte(Object target, String errorMessage) {
        if (isEmpty(target)) {
            return null;
        }

        Byte result = null;
        try {
            if (target instanceof Byte) {
                result = (Byte) target;
            } else if (target instanceof Number) {
                result = ((Number) target).byteValue();
            } else {
                result = Byte.valueOf(target.toString());
            }
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    /**
     * 默认的 Short 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param targetName 目标对象名称
     * @param target     目标对象
     * @return 转化后的数据
     */
    default Short parseShort(String targetName, Object target) {
        return parseShort(target, unexpectedObject("value", target, targetName, Short.class.getSimpleName()));
    }

    /**
     * 默认的 Short 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化后的数据
     */
    default Short parseShort(Object target, String errorMessage) {
        if (isEmpty(target)) {
            return null;
        }

        Short result = null;
        try {
            if (target instanceof Short) {
                result = (Short) target;
            } else if (target instanceof Number) {
                result = ((Number) target).shortValue();
            } else {
                result = Short.valueOf(target.toString());
            }
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    /**
     * 默认的 Integer 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param targetName 目标对象名称
     * @param target     目标对象
     * @return 转化后的数据
     */
    default Integer parseInteger(String targetName, Object target) {
        return parseInteger(target, unexpectedObject("value", target, targetName, Integer.class.getSimpleName()));
    }

    /**
     * 默认的 Integer 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化后的数据
     */
    default Integer parseInteger(Object target, String errorMessage) {
        if (isEmpty(target)) {
            return null;
        }

        Integer result = null;
        try {
            if (target instanceof Integer) {
                result = (Integer) target;
            } else if (target instanceof Number) {
                result = ((Number) target).intValue();
            } else {
                result = Integer.valueOf(target.toString());
            }
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    /**
     * 默认的 Long 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param targetName 目标对象名称
     * @param target     目标对象
     * @return 转化后的数据
     */
    default Long parseLong(String targetName, Object target) {
        return parseLong(target, unexpectedObject("value", target, targetName, Long.class.getSimpleName()));
    }

    /**
     * 默认的 Long 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化后的数据
     */
    default Long parseLong(Object target, String errorMessage) {
        if (isEmpty(target)) {
            return null;
        }

        Long result = null;
        try {
            if (target instanceof Long) {
                result = (Long) target;
            } else if (target instanceof Number) {
                result = ((Number) target).longValue();
            } else {
                result = Long.valueOf(target.toString());
            }
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    /**
     * 默认的 Float 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param targetName 目标对象名称
     * @param target     目标对象
     * @return 转化后的数据
     */
    default Float parseFloat(String targetName, Object target) {
        return parseFloat(target, unexpectedObject("value", target, targetName, Float.class.getSimpleName()));
    }

    /**
     * 默认的 Float 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化后的数据
     */
    default Float parseFloat(Object target, String errorMessage) {
        if (isEmpty(target)) {
            return null;
        }

        Float result = null;
        try {
            if (target instanceof Float) {
                result = (Float) target;
            } else if (target instanceof Number) {
                result = ((Number) target).floatValue();
            } else {
                result = Float.valueOf(target.toString());
            }
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    /**
     * 默认的 Double 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param targetName 目标对象名称
     * @param target     目标对象
     * @return 转化后的数据
     */
    default Double parseDouble(String targetName, Object target) {
        return parseDouble(target, unexpectedObject("value", target, targetName, Double.class.getSimpleName()));
    }

    /**
     * 默认的 Double 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化后的数据
     */
    default Double parseDouble(Object target, String errorMessage) {
        if (isEmpty(target)) {
            return null;
        }

        Double result = null;
        try {
            if (target instanceof Double) {
                result = (Double) target;
            } else if (target instanceof Number) {
                result = ((Number) target).doubleValue();
            } else {
                result = Double.valueOf(target.toString());
            }
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    /**
     * 默认的 BigDecimal 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param targetName 目标对象名称
     * @param target     目标对象
     * @return 转化后的数据
     */
    default BigDecimal parseBigDecimal(String targetName, String target, int scale, RoundingMode roundingMode) {
        return parseBigDecimal(target, scale, roundingMode, unexpectedObject("value", target, targetName, BigDecimal.class.getSimpleName()));
    }

    /**
     * 默认的 BigDecimal 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化后的数据
     */
    default BigDecimal parseBigDecimal(String target, int scale, RoundingMode roundingMode, String errorMessage) {
        if (isEmpty(target)) {
            return null;
        }
        BigDecimal result = null;
        try {
            result = new BigDecimal(target).setScale(scale, roundingMode);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    /**
     * 默认的 Boolean 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     * <p>
     * 下列内容被视为 true：<br/>
     * "1"<br/>
     * "true"<br/>
     * "TRUE"<br/>
     * <p>
     * 下列内容被视为 false：<br/>
     * "0"<br/>
     * "false"<br/>
     * "FALSE"<br/>
     * <p>
     * 其余内容被视为不符合预期
     *
     * @param targetName 目标对象名称
     * @param target     目标对象
     * @return 转化后的数据
     */
    default Boolean parseBoolean(String targetName, Object target) {
        return parseBoolean(target, unexpectedObject("value", target, targetName, "[\"0\",\"1\",\"false\",\"true\",\"FALSE\",\"TRUE\"]"));
    }

    /**
     * 默认的 Boolean 转化函数<br/>
     * 如果目标对象满足 {@link ParameterHelper#isEmpty(Object)} == true<br/>
     * 目标对象将被转化为 null
     * <p>
     * 下列内容被视为 true：<br/>
     * "1"<br/>
     * "true"<br/>
     * "TRUE"<br/>
     * <p>
     * 下列内容被视为 false：<br/>
     * "0"<br/>
     * "false"<br/>
     * "FALSE"<br/>
     * <p>
     * 其余内容被视为不符合预期
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化后的数据
     */
    default Boolean parseBoolean(Object target, String errorMessage) {
        if (target instanceof Boolean) {
            return (Boolean) target;
        }

        String targetStringValue = string(target);
        if (isEmpty(target)) {
            return null;
        }

        Boolean result = null;

        switch (targetStringValue) {
            case "1":
            case "true":
            case "TRUE":
                result = Boolean.TRUE;
                break;
            case "0":
            case "false":
            case "FALSE":
                result = Boolean.FALSE;
                break;
            default:
                hookUnexpectedEvent(errorMessage, null);
        }
        return result;
    }

    /**
     * 转化目标为 (允许为空)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @return 转化成 Boolean 的结果
     */
    Boolean booleanFormat(String targetName, Object target);

    /**
     * 转化目标为 (允许为空)
     *
     * @param targetName   目标对象名称(用于输出默认异常信息)
     * @param target       目标对象
     * @param defaultValue 默认值
     * @return 转化成 Boolean 的结果
     */
    boolean booleanFormatOfNullable(String targetName, Object target, boolean defaultValue);

    /**
     * 转化目标为 (允许为空)
     *
     * @param target       目标对象
     * @param defaultValue 默认值
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Boolean 的结果
     */
    boolean booleanFormatOfNullable(Object target, boolean defaultValue, String errorMessage);

    /**
     * 转化目标为 (不许为空)
     *
     * @param targetName 目标对象名称(用于输出默认异常信息)
     * @param target     目标对象
     * @return 转化成 Boolean 的结果
     */
    boolean booleanFormatNotEmpty(String targetName, Object target);

    /**
     * 转化目标为 (不许为空)
     *
     * @param target       目标对象
     * @param errorMessage 不符合预期时的完整异常提示信息
     * @return 转化成 Boolean 的结果
     */
    boolean booleanFormatNotEmpty(Object target, String errorMessage);

    /**
     * 从 target 左侧填充若干个 fillingItem 直至长度到达 totalLength
     *
     * @param target               待填充的目标
     * @param totalLength          填充结束最终长度
     * @param fillingItem          填充单位
     * @param stringFormatModeEnum 填充结果处理模式
     * @return 填充后的结果
     */
    String leftFillString(String target, int totalLength, String fillingItem, StringFormatModeEnum stringFormatModeEnum);

    /**
     * 从 target 右侧填充若干个 fillingItem 直至长度到达 totalLength
     *
     * @param target               待填充的目标
     * @param totalLength          填充结束最终长度
     * @param fillingItem          填充单位
     * @param stringFormatModeEnum 填充结果处理模式
     * @return 填充后的结果
     */
    String rightFillString(String target, int totalLength, String fillingItem, StringFormatModeEnum stringFormatModeEnum);

    default String formatString(StringFormatModeEnum stringFormatModeEnum, String string) {
        String result;
        switch (stringFormatModeEnum) {
            case LOWERCASE:
                result = string.toLowerCase();
                break;
            case UPPERCASE:
                result = string.toUpperCase();
                break;
            default:
                result = string;
        }
        return result;
    }

    /**
     * 将传入字符串首字母大写
     *
     * @param target 目标字符串
     * @return 将其首字母大写后的字符串
     */
    String upperCaseFirstLetter(String target);

    /**
     * 将传入字符串首字母小写
     *
     * @param target 目标字符串
     * @return 将其首字母小写后的字符串
     */
    String lowerCaseFirstLetter(String target);

    /**
     * 从尾部删除目标字符(主要用于清除结尾重复字符)
     *
     * @param target 待删除内容的目标
     * @param string 删除目标
     * @param time   最大删除次数(不小于0)
     * @return 删除特定字符后的目标
     */
    String removeStringFormTail(String target, String string, int time);

    /**
     * 从尾部删除目标字符(主要用于清除结尾重复字符)
     *
     * @param target 待删除内容的目标
     * @param string 删除目标
     * @param time   最大删除次数(不小于0)
     * @return 删除特定字符后的目标
     */
    StringBuilder removeStringFormTail(StringBuilder target, String string, int time);

    /**
     * 逐行扫描，从数据源中截取开始标记、结束标记之间的全部行内容<br/>
     * 数据源：
     * <pre>
     *     asdzxcasfasd
     *     S-
     *     textContent
     *     -E
     *     wersgsdfasda
     * </pre>
     * <p>
     * 执行函数：<br/>
     * <pre>
     *     getTextFileSpeContent(source, "S-", "-E")
     * </pre>
     * <p>
     * 结果：<br/>
     * <pre>
     *     S-
     *     textContent
     *     -E
     * </pre>
     *
     * @param source    待提取内容的数据源
     * @param startMark 开始标记
     * @param endMark   结束标记
     * @return 开始标记、结束标记之间的全部行内容。未找到完整开始标记、结束标记时，返回 null
     */
    StringBuilder getTextFileSpeContent(StringBuilder source, String startMark, String endMark);
}
