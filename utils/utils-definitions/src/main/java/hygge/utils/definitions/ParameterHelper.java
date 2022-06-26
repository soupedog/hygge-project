package hygge.utils.definitions;

import hygge.commons.enums.StringFormatModeEnum;
import hygge.utils.HyggeUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 参数校验工具类
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface ParameterHelper extends HyggeUtil {
    /**
     * 首字母大写常规模式，入参字符串最小长度
     */
    int UPPER_CASE_FIRST_LETTER_MIN_LENGTH = 2;
    /**
     * 首字母小写常规模式，入参字符串最小长度
     */
    int LOWER_CASE_FIRST_LETTER_MIN_LENGTH = 2;

    String LINE_FEED = "\r\n";

    /**
     * 验证目标是否为空
     *
     * @param target 被验证的目标
     * @return true: null 或者 空串
     */
    boolean isEmpty(Object target);

    /**
     * 验证目标是否全部为空
     *
     * @param targetArray 被验证的目标
     * @return true: 全部对象 null 或者 空串
     */
    boolean isEmptyAll(Object... targetArray);

    /**
     * 验证目标是否至少一个不为空
     *
     * @param targetArray 被验证的目标
     * @return true: 至少有一个对象非 null 、非空串
     */
    boolean atLeastOneNotEmpty(Object... targetArray);

    /**
     * 验证目标是否全部不为空
     *
     * @param targetArray 被验证的目标
     * @return true: 全部不为空
     */
    boolean isAllNotEmpty(Object... targetArray);

    /**
     * 验证目标是否为非空
     *
     * @param target 被验证的目标
     * @return true: 非 null 、非空串
     */
    boolean isNotEmpty(Object target);

    /**
     * String 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    String hookString(String resultTemp);

    /**
     * Boolean 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    Boolean hookBoolean(Boolean resultTemp);

    /**
     * Byte 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    Byte hookByte(Byte resultTemp);

    /**
     * Short 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    Short hookShort(Short resultTemp);

    /**
     * Integer 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    Integer hookInteger(Integer resultTemp);

    /**
     * Long 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    Long hookLong(Long resultTemp);

    /**
     * Float 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    Float hookFloat(Float resultTemp);

    /**
     * Double 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    Double hookDouble(Double resultTemp);

    /**
     * BigDecimal 相关方法，处理返回结果的钩子函数
     *
     * @param resultTemp 原定返回结果
     * @return 最终返回结果
     */
    BigDecimal hookBigDecimal(BigDecimal resultTemp);

    /**
     * 默认异常处理函数
     *
     * @param errorMessage 不符合限制条件时的异常提示
     * @param throwable    触发非预期事件的异常(可能为空)
     */
    void hookUnexpectedEvent(String errorMessage, Throwable throwable);

    /**
     * 获取目标对象，如果目标对象为 null 则返回 defaultValue
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       被获取的入参
     * @param defaultValue 入参为 null 时返回的默认值
     * @param <T>          返回值类型
     * @return 目标对象不为 null 时为自己本身，否则为 defaultValue
     */
    <T> T getObjectOfNullable(String targetName, Object target, T defaultValue);

    /**
     * 要求目标对象不为 null
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待验证的入参
     */
    void objectNotNull(String targetName, Object target);

    /**
     * 要求目标对象不为 null
     *
     * @param target       待验证的入参
     * @param errorMessage 不符合限制条件时的异常提示
     */
    void objectNotNull(Object target, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param target 入参
     * @return 转化成字符串的入参
     */
    String string(Object target);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @return 转化成字符串的入参
     */
    String stringOfNullable(Object target, String defaultValue);

    /**
     * 将入参进行类型转换(允许入参为空)，且对字符串长度进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param minLength  最小字符串长度
     * @param maxLength  最大字符串长度
     * @return 转化成字符串的入参
     */
    String string(String targetName, Object target, int minLength, int maxLength);

    /**
     * 将入参进行类型转换(允许入参为空)，且对字符串长度进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param minLength    最小字符串长度
     * @param maxLength    最大字符串长度
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成字符串的入参
     */
    String string(Object target, int minLength, int maxLength, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target     待转化目标
     * @param targetName 入参名称(用于输出默认异常信息)
     * @return 转化成字符串的入参
     */
    String stringNotEmpty(String targetName, Object target);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target       待转化目标
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成字符串的入参
     */
    String stringNotEmpty(Object target, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对字符串长度进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param minLength  最小字符串长度
     * @param maxLength  最大字符串长度
     * @return 转化成字符串的入参
     */
    String stringNotEmpty(String targetName, Object target, int minLength, int maxLength);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对字符串长度进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param minLength    最小字符串长度
     * @param maxLength    最大字符串长度
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成字符串的入参
     */
    String stringNotEmpty(Object target, int minLength, int maxLength, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @return 转化成 Byte 的入参
     */
    Byte byteFormat(String targetName, Object target);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @return 转化成 Byte 的入参
     */
    Byte byteFormatOfNullable(String targetName, Object target, byte defaultValue);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Byte 的入参
     */
    Byte byteFormatOfNullable(Object target, byte defaultValue, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Byte 的入参
     */
    Byte byteFormat(String targetName, Object target, byte min, byte max);

    /**
     * 将入参进行类型转换(允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Byte 的入参
     */
    Byte byteFormat(Object target, byte min, byte max, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target     待转化目标
     * @param targetName 入参名称(用于输出默认异常信息)
     * @return 转化成 Byte 的入参
     */
    Byte byteFormatNotEmpty(String targetName, Object target);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target       待转化目标
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Byte 的入参
     */
    Byte byteFormatNotEmpty(Object target, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Byte 的入参
     */
    Byte byteFormatNotEmpty(String targetName, Object target, byte min, byte max);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Byte 的入参
     */
    Byte byteFormatNotEmpty(Object target, byte min, byte max, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @return 转化成 Short 的入参
     */
    Short shortFormat(String targetName, Object target);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @return 转化成 Short 的入参
     */
    Short shortFormatOfNullable(String targetName, Object target, short defaultValue);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Short 的入参
     */
    Short shortFormat(Object target, short defaultValue, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Short 的入参
     */
    Short shortFormat(String targetName, Object target, short min, short max);

    /**
     * 将入参进行类型转换(允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Short 的入参
     */
    Short shortFormat(Object target, short min, short max, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target     待转化目标
     * @param targetName 入参名称(用于输出默认异常信息)
     * @return 转化成 Short 的入参
     */
    Short shortFormatNotEmpty(String targetName, Object target);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target       待转化目标
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Short 的入参
     */
    Short shortFormatNotEmpty(Object target, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Short 的入参
     */
    Short shortFormatNotEmpty(String targetName, Object target, short min, short max);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Short 的入参
     */
    Short shortFormatNotEmpty(Object target, short min, short max, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @return 转化成 Integer 的入参
     */
    Integer integerFormat(String targetName, Object target);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @return 转化成 Integer 的入参
     */
    Integer integerFormatOfNullable(String targetName, Object target, int defaultValue);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Integer 的入参
     */
    Integer integerFormat(Object target, int defaultValue, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Integer 的入参
     */
    Integer integerFormat(String targetName, Object target, int min, int max);

    /**
     * 将入参进行类型转换(允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Integer 的入参
     */
    Integer integerFormat(Object target, int min, int max, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target     待转化目标
     * @param targetName 入参名称(用于输出默认异常信息)
     * @return 转化成 Integer 的入参
     */
    Integer integerFormatNotEmpty(String targetName, Object target);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target       待转化目标
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Integer 的入参
     */
    Integer integerFormatNotEmpty(Object target, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Integer 的入参
     */
    Integer integerFormatNotEmpty(String targetName, Object target, int min, int max);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Integer 的入参
     */
    Integer integerFormatNotEmpty(Object target, int min, int max, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @return 转化成 Long 的入参
     */
    Long longFormat(String targetName, Object target);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @return 转化成 Long 的入参
     */
    Long longFormatOfNullable(String targetName, Object target, long defaultValue);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Long 的入参
     */
    Long longFormat(Object target, long defaultValue, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Long 的入参
     */
    Long longFormat(String targetName, Object target, long min, long max);

    /**
     * 将入参进行类型转换(允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Long 的入参
     */
    Long longFormat(Object target, long min, long max, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target     待转化目标
     * @param targetName 入参名称(用于输出默认异常信息)
     * @return 转化成 Long 的入参
     */
    Long longFormatNotEmpty(String targetName, Object target);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target       待转化目标
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Long 的入参
     */
    Long longFormatNotEmpty(Object target, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Long 的入参
     */
    Long longFormatNotEmpty(String targetName, Object target, long min, long max);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Long 的入参
     */
    Long longFormatNotEmpty(Object target, long min, long max, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @return 转化成 Float 的入参
     */
    Float floatFormat(String targetName, Object target);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @return 转化成 Float 的入参
     */
    Float floatFormatOfNullable(String targetName, Object target, float defaultValue);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Float 的入参
     */
    Float floatFormat(Object target, float defaultValue, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Float 的入参
     */
    Float floatFormat(String targetName, Object target, float min, float max);

    /**
     * 将入参进行类型转换(允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Float 的入参
     */
    Float floatFormat(Object target, float min, float max, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target     待转化目标
     * @param targetName 入参名称(用于输出默认异常信息)
     * @return 转化成 Float 的入参
     */
    Float floatFormatNotEmpty(String targetName, Object target);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target       待转化目标
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Float 的入参
     */
    Float floatFormatNotEmpty(Object target, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Float 的入参
     */
    Float floatFormatNotEmpty(String targetName, Object target, float min, float max);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Float 的入参
     */
    Float floatFormatNotEmpty(Object target, float min, float max, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @return 转化成 Double 的入参
     */
    Double doubleFormat(String targetName, Object target);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @return 转化成 Double 的入参
     */
    Double doubleFormatOfNullable(String targetName, Object target, double defaultValue);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param target       待转化目标
     * @param defaultValue 入参为 null 或 空字符串时的默认值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Double 的入参
     */
    Double doubleFormatOfNullable(Object target, double defaultValue, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Double 的入参
     */
    Double doubleFormat(String targetName, Object target, double min, double max);

    /**
     * 将入参进行类型转换(允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Double 的入参
     */
    Double doubleFormat(Object target, double min, double max, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target     待转化目标
     * @param targetName 入参名称(用于输出默认异常信息)
     * @return 转化成 Double 的入参
     */
    Double doubleFormatNotEmpty(String targetName, Object target);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target       待转化目标
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Double 的入参
     */
    Double doubleFormatNotEmpty(Object target, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @param min        最小值
     * @param max        最大值
     * @return 转化成 Double 的入参
     */
    Double doubleFormatNotEmpty(String targetName, Object target, double min, double max);

    /**
     * 将入参进行类型转换(不允许入参为空)，且对其值进行限制(闭区间)
     *
     * @param target       待转化目标
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Double 的入参
     */
    Double doubleFormatNotEmpty(Object target, double min, double max, String errorMessage);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       待转化目标
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @return 转化成 BigDecimal 的入参
     */
    BigDecimal bigDecimalFormat(String targetName, String target, int scale, RoundingMode roundingMode);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       待转化目标
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param defaultValue 默认值
     * @return 转化成 BigDecimal 的入参
     */
    BigDecimal bigDecimalFormatOfNullable(String targetName, String target, int scale, RoundingMode roundingMode, BigDecimal defaultValue);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param target       待转化目标
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param defaultValue 默认值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 BigDecimal 的入参
     */
    BigDecimal bigDecimalFormatOfNullable(String target, int scale, RoundingMode roundingMode, BigDecimal defaultValue, String errorMessage);


    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       待转化目标
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param min          最小值
     * @param max          最大值
     * @return 转化成 BigDecimal 的入参
     */
    BigDecimal bigDecimalFormat(String targetName, String target, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param target       待转化目标
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 BigDecimal 的入参
     */
    BigDecimal bigDecimalFormat(String target, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max, String errorMessage);


    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       待转化目标
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @return 转化成 BigDecimal 的入参
     */
    BigDecimal bigDecimalFormatNotEmpty(String targetName, String target, int scale, RoundingMode roundingMode);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target       待转化目标
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 BigDecimal 的入参
     */
    BigDecimal bigDecimalFormatNotEmpty(String target, int scale, RoundingMode roundingMode, String errorMessage);


    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       待转化目标
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param min          最小值
     * @param max          最大值
     * @return 转化成 BigDecimal 的入参
     */
    BigDecimal bigDecimalFormatNotEmpty(String targetName, String target, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max);


    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target       待转化目标
     * @param scale        小数点位数
     * @param roundingMode 精度保留模式
     * @param min          最小值
     * @param max          最大值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 BigDecimal 的入参
     */
    BigDecimal bigDecimalFormatNotEmpty(String target, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max, String errorMessage);


    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @return 转化成 Boolean 的入参
     */
    Boolean booleanFormat(String targetName, Object target);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param targetName   入参名称(用于输出默认异常信息)
     * @param target       待转化目标
     * @param defaultValue 默认值
     * @return 转化成 Boolean 的入参
     */
    Boolean booleanFormatOfNullable(String targetName, Object target, Boolean defaultValue);

    /**
     * 将入参进行类型转换(允许入参为空)
     *
     * @param target       待转化目标
     * @param defaultValue 默认值
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Boolean 的入参
     */
    Boolean booleanFormatOfNullable(Object target, Boolean defaultValue, String errorMessage);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param targetName 入参名称(用于输出默认异常信息)
     * @param target     待转化目标
     * @return 转化成 Boolean 的入参
     */
    Boolean booleanFormatNotEmpty(String targetName, Object target);

    /**
     * 将入参进行类型转换(不允许入参为空)
     *
     * @param target       待转化目标
     * @param errorMessage 不符合限制条件时的异常提示
     * @return 转化成 Boolean 的入参
     */
    Boolean booleanFormatNotEmpty(Object target, String errorMessage);

    /**
     * 从 target 左侧填充若干个 fillingItem 直至长度到达 totalLength
     *
     * @param target               被填充的对象
     * @param totalLength          填充结束最终长度
     * @param fillingItem          填充单位
     * @param stringFormatModeEnum 填充结果处理模式
     * @return 填充后的结果
     */
    String leftFillString(String target, int totalLength, String fillingItem, StringFormatModeEnum stringFormatModeEnum);

    /**
     * 从 target 右侧填充若干个 fillingItem 直至长度到达 totalLength
     *
     * @param target               被填充的对象
     * @param totalLength          填充结束最终长度
     * @param fillingItem          填充单位
     * @param stringFormatModeEnum 填充结果处理模式
     * @return 填充后的结果
     */
    String rightFillString(String target, int totalLength, String fillingItem, StringFormatModeEnum stringFormatModeEnum);


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
     * @param target 待删除的目标
     * @param string 删除目标
     * @param time   最大删除次数
     * @return 删除特定字符后的目标
     */
    String removeStringFormTail(String target, String string, int time);

    /**
     * 从尾部删除目标字符(主要用于清除结尾重复字符)
     *
     * @param target 待删除的目标
     * @param string 删除目标
     * @param time   最大删除次数
     * @return 删除特定字符后的目标
     */
    StringBuilder removeStringFormTail(StringBuilder target, String string, int time);

    /**
     * 截取数据源中开始标记、结束标记之间的文字内容<br/>
     * {@code source:"其他内容start>测试内容<end其他内容"  startMark:"start>" endMark:"<end" result:"start>测试内容<end" }
     *
     * @param source    待提取内容的数据源
     * @param startMark 开始标记
     * @param endMark   结束标记
     * @return 开始标记、结束标记之间的截取内容。未找到完整开始标记、结束标记时，返回 null
     */
    StringBuilder getTextFileSpeContent(StringBuilder source, String startMark, String endMark);
}
