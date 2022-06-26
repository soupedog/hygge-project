package hygge.utils.base;


import hygge.commons.enums.StringFormatModeEnum;
import hygge.commons.exceptions.ParameterRuntimeException;
import hygge.utils.definitions.ParameterHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * 参数校验工具类基类
 *
 * @author Xavier
 * @date 2022/6/26
 * @since 1.0
 */
public abstract class BaseParameterHelper implements ParameterHelper {
    @Override
    public boolean isEmpty(Object target) {
        boolean result = true;
        if (target != null) {
            if (target instanceof String) {
                result = ((String) target).trim().isEmpty();
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

    @Override
    public boolean isEmptyAll(Object... targetArray) {
        for (Object item : targetArray) {
            if (isNotEmpty(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean atLeastOneNotEmpty(Object... targetArray) {
        return !isEmptyAll(targetArray);
    }

    @Override
    public boolean isAllNotEmpty(Object... targetArray) {
        for (Object item : targetArray) {
            if (isEmpty(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isNotEmpty(Object target) {
        return !isEmpty(target);
    }

    @Override
    public void hookUnexpectedEvent(String errorMessage, Throwable throwable) {
        throw new ParameterRuntimeException(errorMessage, throwable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getObjectOfNullable(String targetName, Object target, T defaultValue) {
        T result = null;
        if (target == null) {
            result = defaultValue;
        } else {
            if (defaultValue == null) {
                hookUnexpectedEvent("Unexpected defaultValue,it can't be null.", null);
            } else {
                if (defaultValue.getClass().isAssignableFrom(target.getClass())) {
                    result = (T) target;
                } else {
                    hookUnexpectedEvent(String.format("Unexpected %s,it should be a instance of %s.", targetName, defaultValue.getClass()), null);
                }
            }
        }
        return result;
    }

    @Override
    public void objectNotNull(String targetName, Object target) {
        if (target == null) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be null.", targetName), null);
        }
    }

    @Override
    public void objectNotNull(Object target, String errorMessage) {
        if (target == null) {
            hookUnexpectedEvent(errorMessage, null);
        }
    }

    @Override
    public String string(Object target) {
        if (isEmpty(target)) {
            return hookString(null);
        }
        return hookString(target.toString());
    }

    @Override
    public String stringOfNullable(Object target, String defaultValue) {
        if (isEmpty(target)) {
            return hookString(defaultValue);
        }
        String result = target.toString();
        return hookString(result);
    }

    @Override
    public String string(String targetName, Object target, int minLength, int maxLength) {
        String result = null;
        if (isEmpty(target)) {
            if (0 < minLength) {
                hookUnexpectedEvent(String.format("Unexpected %s,its length needs to be between %d and %d(closed interval),not %d.",
                        targetName,
                        minLength,
                        maxLength,
                        0),
                        null);
            }
        } else {
            result = Objects.requireNonNull(target).toString();
            int resultLength = result.length();
            if (resultLength < minLength || resultLength > maxLength) {
                hookUnexpectedEvent(String.format("Unexpected %s,its length needs to be between %d and %d(closed interval),not %d.",
                        targetName,
                        minLength,
                        maxLength,
                        resultLength),
                        null);
            }
        }
        return hookString(result);
    }

    @Override
    public String string(Object target, int minLength, int maxLength, String errorMessage) {
        String result = null;
        if (isEmpty(target)) {
            if (0 < minLength) {
                hookUnexpectedEvent(errorMessage, null);
            }
        } else {
            result = Objects.requireNonNull(target).toString();
            int resultLength = result.length();
            if (resultLength < minLength || resultLength > maxLength) {
                hookUnexpectedEvent(errorMessage, null);
            }
        }
        return hookString(result);
    }

    @Override
    public String stringNotEmpty(String targetName, Object target) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        String resultTemp = Objects.requireNonNull(target).toString();
        return hookString(resultTemp);
    }

    @Override
    public String stringNotEmpty(Object target, String errorMessage) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        String resultTemp = Objects.requireNonNull(target).toString();
        return hookString(resultTemp);
    }

    @Override
    public String stringNotEmpty(String targetName, Object target, int minLength, int maxLength) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        String resultTemp = Objects.requireNonNull(target).toString();
        int resultLength = resultTemp.length();
        if (resultLength < minLength || resultLength > maxLength) {
            hookUnexpectedEvent(String.format("Unexpected %s,its length needs to be between %d and %d(closed interval),not %d.",
                    targetName,
                    minLength,
                    maxLength,
                    resultLength),
                    null);
        }
        return hookString(resultTemp);
    }

    @Override
    public String stringNotEmpty(Object target, int minLength, int maxLength, String errorMessage) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        String resultTemp = Objects.requireNonNull(target).toString();
        int resultTempLength = resultTemp.length();
        if (resultTempLength < minLength || resultTempLength > maxLength) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookString(resultTemp);
    }

    @Override
    public Byte byteFormat(String targetName, Object target) {
        if (isEmpty(target)) {
            return hookByte(null);
        }
        Byte result = null;
        try {
            if (target instanceof Byte) {
                result = (Byte) target;
            } else if (target instanceof Number) {
                result = ((Number) target).byteValue();
            } else {
                String targetStringValue = target.toString();
                result = Byte.valueOf(targetStringValue);
            }
            result = hookByte(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Byte value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Byte byteFormatOfNullable(String targetName, Object target, byte defaultValue) {
        if (isEmpty(target)) {
            return hookByte(defaultValue);
        }
        Byte result = null;
        try {
            if (target instanceof Byte) {
                result = (Byte) target;
            } else if (target instanceof Number) {
                result = ((Number) target).byteValue();
            } else {
                String targetStringValue = target.toString();
                result = Byte.valueOf(targetStringValue);
            }
            result = hookByte(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Byte value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Byte byteFormatOfNullable(Object target, byte defaultValue, String errorMessage) {
        if (isEmpty(target)) {
            return hookByte(defaultValue);
        }
        Byte result = null;
        try {
            if (target instanceof Byte) {
                result = (Byte) target;
            } else if (target instanceof Number) {
                result = ((Number) target).byteValue();
            } else {
                String targetStringValue = target.toString();
                result = Byte.valueOf(targetStringValue);
            }
            result = hookByte(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Byte byteFormat(String targetName, Object target, byte min, byte max) {
        Byte result = null;
        if (isEmpty(target)) {
            return hookByte(null);
        }
        try {
            if (target instanceof Byte) {
                result = (Byte) target;
            } else if (target instanceof Number) {
                result = ((Number) target).byteValue();
            } else {
                String targetStringValue = target.toString();
                result = Byte.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        String.valueOf(min),
                        String.valueOf(max),
                        target.toString()
                        ),
                        null);
            }
            result = hookByte(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Byte value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Byte byteFormat(Object target, byte min, byte max, String errorMessage) {
        Byte result = null;
        if (isEmpty(target)) {
            return hookByte(null);
        }
        try {
            if (target instanceof Byte) {
                result = (Byte) target;
            } else if (target instanceof Number) {
                result = ((Number) target).byteValue();
            } else {
                String targetStringValue = target.toString();
                result = Byte.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookByte(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Byte byteFormatNotEmpty(String targetName, Object target) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        Byte result = null;
        try {
            if (target instanceof Byte) {
                result = (Byte) target;
            } else if (target instanceof Number) {
                result = ((Number) target).byteValue();
            } else {
                String targetStringValue = target.toString();
                result = Byte.valueOf(targetStringValue);
            }
            result = hookByte(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Byte value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Byte byteFormatNotEmpty(Object target, String errorMessage) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        Byte result = null;
        try {
            if (target instanceof Byte) {
                result = (Byte) target;
            } else if (target instanceof Number) {
                result = ((Number) target).byteValue();
            } else {
                String targetStringValue = target.toString();
                result = Byte.valueOf(targetStringValue);
            }
            result = hookByte(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Byte byteFormatNotEmpty(String targetName, Object target, byte min, byte max) {
        Byte result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        try {
            if (target instanceof Byte) {
                result = (Byte) target;
            } else if (target instanceof Number) {
                result = ((Number) target).byteValue();
            } else {
                String targetStringValue = target.toString();
                result = Byte.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        String.valueOf(min),
                        String.valueOf(max),
                        target.toString()
                        ),
                        null);
            }
            result = hookByte(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Byte value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Byte byteFormatNotEmpty(Object target, byte min, byte max, String errorMessage) {
        Byte result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        try {
            if (target instanceof Byte) {
                result = (Byte) target;
            } else if (target instanceof Number) {
                result = ((Number) target).byteValue();
            } else {
                String targetStringValue = target.toString();
                result = Byte.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookByte(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Short shortFormat(String targetName, Object target) {
        if (isEmpty(target)) {
            return hookShort(null);
        }
        Short result = null;
        try {
            if (target instanceof Short) {
                result = (Short) target;
            } else if (target instanceof Number) {
                result = ((Number) target).shortValue();
            } else {
                String targetStringValue = target.toString();
                result = Short.valueOf(targetStringValue);
            }
            result = hookShort(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Short value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Short shortFormatOfNullable(String targetName, Object target, short defaultValue) {
        if (isEmpty(target)) {
            return hookShort(defaultValue);
        }
        Short result = null;
        try {
            if (target instanceof Short) {
                result = (Short) target;
            } else if (target instanceof Number) {
                result = ((Number) target).shortValue();
            } else {
                String targetStringValue = target.toString();
                result = Short.valueOf(targetStringValue);
            }
            result = hookShort(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Short value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Short shortFormat(Object target, short defaultValue, String errorMessage) {
        if (isEmpty(target)) {
            return hookShort(defaultValue);
        }
        Short result = null;
        try {
            if (target instanceof Short) {
                result = (Short) target;
            } else if (target instanceof Number) {
                result = ((Number) target).shortValue();
            } else {
                String targetStringValue = target.toString();
                result = Short.valueOf(targetStringValue);
            }
            result = hookShort(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Short shortFormat(String targetName, Object target, short min, short max) {
        Short result = null;
        if (isEmpty(target)) {
            return hookShort(null);
        }
        try {
            if (target instanceof Short) {
                result = (Short) target;
            } else if (target instanceof Number) {
                result = ((Number) target).shortValue();
            } else {
                String targetStringValue = target.toString();
                result = Short.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        String.valueOf(min),
                        String.valueOf(max),
                        target.toString()
                        ),
                        null);
            }
            result = hookShort(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Short value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Short shortFormat(Object target, short min, short max, String errorMessage) {
        Short result = null;
        if (isEmpty(target)) {
            return hookShort(null);
        }
        try {
            if (target instanceof Short) {
                result = (Short) target;
            } else if (target instanceof Number) {
                result = ((Number) target).shortValue();
            } else {
                String targetStringValue = target.toString();
                result = Short.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookShort(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Short shortFormatNotEmpty(String targetName, Object target) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        Short result = null;
        try {
            if (target instanceof Short) {
                result = (Short) target;
            } else if (target instanceof Number) {
                result = ((Number) target).shortValue();
            } else {
                String targetStringValue = target.toString();
                result = Short.valueOf(targetStringValue);
            }
            result = hookShort(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Short value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Short shortFormatNotEmpty(Object target, String errorMessage) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        Short result = null;
        try {
            if (target instanceof Short) {
                result = (Short) target;
            } else if (target instanceof Number) {
                result = ((Number) target).shortValue();
            } else {
                String targetStringValue = target.toString();
                result = Short.valueOf(targetStringValue);
            }
            result = hookShort(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Short shortFormatNotEmpty(String targetName, Object target, short min, short max) {
        Short result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        try {
            if (target instanceof Short) {
                result = (Short) target;
            } else if (target instanceof Number) {
                result = ((Number) target).shortValue();
            } else {
                String targetStringValue = target.toString();
                result = Short.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        String.valueOf(min),
                        String.valueOf(max),
                        target.toString()
                        ),
                        null);
            }
            result = hookShort(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Short value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Short shortFormatNotEmpty(Object target, short min, short max, String errorMessage) {
        Short result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        try {
            if (target instanceof Short) {
                result = (Short) target;
            } else if (target instanceof Number) {
                result = ((Number) target).shortValue();
            } else {
                String targetStringValue = target.toString();
                result = Short.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookShort(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Integer integerFormat(String targetName, Object target) {
        if (isEmpty(target)) {
            return hookInteger(null);
        }
        Integer result = null;
        try {
            if (target instanceof Integer) {
                result = (Integer) target;
            } else if (target instanceof Number) {
                result = ((Number) target).intValue();
            } else {
                String targetStringValue = target.toString();
                result = Integer.valueOf(targetStringValue);
            }
            result = hookInteger(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Integer value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Integer integerFormatOfNullable(String targetName, Object target, int defaultValue) {
        if (isEmpty(target)) {
            return hookInteger(defaultValue);
        }
        Integer result = null;
        try {
            if (target instanceof Integer) {
                result = (Integer) target;
            } else if (target instanceof Number) {
                result = ((Number) target).intValue();
            } else {
                String targetStringValue = target.toString();
                result = Integer.valueOf(targetStringValue);
            }
            result = hookInteger(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Integer value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Integer integerFormat(Object target, int defaultValue, String errorMessage) {
        if (isEmpty(target)) {
            return hookInteger(defaultValue);
        }
        Integer result = null;
        try {
            if (target instanceof Integer) {
                result = (Integer) target;
            } else if (target instanceof Number) {
                result = ((Number) target).intValue();
            } else {
                String targetStringValue = target.toString();
                result = Integer.valueOf(targetStringValue);
            }
            result = hookInteger(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Integer integerFormat(String targetName, Object target, int min, int max) {
        Integer result = null;
        if (isEmpty(target)) {
            return hookInteger(null);
        }
        try {
            if (target instanceof Integer) {
                result = (Integer) target;
            } else if (target instanceof Number) {
                result = ((Number) target).intValue();
            } else {
                String targetStringValue = target.toString();
                result = Integer.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        min,
                        max,
                        target.toString()
                        ),
                        null);
            }
            result = hookInteger(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Integer value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Integer integerFormat(Object target, int min, int max, String errorMessage) {
        Integer result = null;
        if (isEmpty(target)) {
            return hookInteger(null);
        }
        try {
            if (target instanceof Integer) {
                result = (Integer) target;
            } else if (target instanceof Number) {
                result = ((Number) target).intValue();
            } else {
                String targetStringValue = target.toString();
                result = Integer.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookInteger(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Integer integerFormatNotEmpty(String targetName, Object target) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        Integer result = null;
        try {
            if (target instanceof Integer) {
                result = (Integer) target;
            } else if (target instanceof Number) {
                result = ((Number) target).intValue();
            } else {
                String targetStringValue = target.toString();
                result = Integer.valueOf(targetStringValue);
            }
            result = hookInteger(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Integer value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Integer integerFormatNotEmpty(Object target, String errorMessage) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        Integer result = null;
        try {
            if (target instanceof Integer) {
                result = (Integer) target;
            } else if (target instanceof Number) {
                result = ((Number) target).intValue();
            } else {
                String targetStringValue = target.toString();
                result = Integer.valueOf(targetStringValue);
            }
            result = hookInteger(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Integer integerFormatNotEmpty(String targetName, Object target, int min, int max) {
        Integer result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        try {
            if (target instanceof Integer) {
                result = (Integer) target;
            } else if (target instanceof Number) {
                result = ((Number) target).intValue();
            } else {
                String targetStringValue = target.toString();
                result = Integer.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        min,
                        max,
                        target.toString()
                        ),
                        null);
            }
            result = hookInteger(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Integer value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Integer integerFormatNotEmpty(Object target, int min, int max, String errorMessage) {
        Integer result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        try {
            if (target instanceof Integer) {
                result = (Integer) target;
            } else if (target instanceof Number) {
                result = ((Number) target).intValue();
            } else {
                String targetStringValue = target.toString();
                result = Integer.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookInteger(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Long longFormat(String targetName, Object target) {
        if (isEmpty(target)) {
            return hookLong(null);
        }
        Long result = null;
        try {
            if (target instanceof Long) {
                result = (Long) target;
            } else if (target instanceof Number) {
                result = ((Number) target).longValue();
            } else {
                String targetStringValue = target.toString();
                result = Long.valueOf(targetStringValue);
            }
            result = hookLong(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Long value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Long longFormatOfNullable(String targetName, Object target, long defaultValue) {
        if (isEmpty(target)) {
            return hookLong(defaultValue);
        }
        Long result = null;
        try {
            if (target instanceof Long) {
                result = (Long) target;
            } else if (target instanceof Number) {
                result = ((Number) target).longValue();
            } else {
                String targetStringValue = target.toString();
                result = Long.valueOf(targetStringValue);
            }
            result = hookLong(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Long value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Long longFormat(Object target, long defaultValue, String errorMessage) {
        if (isEmpty(target)) {
            return hookLong(defaultValue);
        }
        Long result = null;
        try {
            if (target instanceof Long) {
                result = (Long) target;
            } else if (target instanceof Number) {
                result = ((Number) target).longValue();
            } else {
                String targetStringValue = target.toString();
                result = Long.valueOf(targetStringValue);
            }
            result = hookLong(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Long longFormat(String targetName, Object target, long min, long max) {
        Long result = null;
        if (isEmpty(target)) {
            return hookLong(null);
        }
        try {
            if (target instanceof Long) {
                result = (Long) target;
            } else if (target instanceof Number) {
                result = ((Number) target).longValue();
            } else {
                String targetStringValue = target.toString();
                result = Long.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        min,
                        max,
                        target.toString()
                        ),
                        null);
            }
            result = hookLong(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Long value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Long longFormat(Object target, long min, long max, String errorMessage) {
        Long result = null;
        if (isEmpty(target)) {
            return hookLong(null);
        }
        try {
            if (target instanceof Long) {
                result = (Long) target;
            } else if (target instanceof Number) {
                result = ((Number) target).longValue();
            } else {
                String targetStringValue = target.toString();
                result = Long.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookLong(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Long longFormatNotEmpty(String targetName, Object target) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        Long result = null;
        try {
            if (target instanceof Long) {
                result = (Long) target;
            } else if (target instanceof Number) {
                result = ((Number) target).longValue();
            } else {
                String targetStringValue = target.toString();
                result = Long.valueOf(targetStringValue);
            }
            result = hookLong(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Long value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Long longFormatNotEmpty(Object target, String errorMessage) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        Long result = null;
        try {
            if (target instanceof Long) {
                result = (Long) target;
            } else if (target instanceof Number) {
                result = ((Number) target).longValue();
            } else {
                String targetStringValue = target.toString();
                result = Long.valueOf(targetStringValue);
            }
            result = hookLong(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Long longFormatNotEmpty(String targetName, Object target, long min, long max) {
        Long result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        try {
            if (target instanceof Long) {
                result = (Long) target;
            } else if (target instanceof Number) {
                result = ((Number) target).longValue();
            } else {
                String targetStringValue = target.toString();
                result = Long.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        min,
                        max,
                        target.toString()
                        ),
                        null);
            }
            result = hookLong(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Long value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Long longFormatNotEmpty(Object target, long min, long max, String errorMessage) {
        Long result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        try {
            if (target instanceof Long) {
                result = (Long) target;
            } else if (target instanceof Number) {
                result = ((Number) target).longValue();
            } else {
                String targetStringValue = target.toString();
                result = Long.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookLong(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Float floatFormat(String targetName, Object target) {
        if (isEmpty(target)) {
            return hookFloat(null);
        }
        Float result = null;
        try {
            if (target instanceof Float) {
                result = (Float) target;
            } else if (target instanceof Number) {
                result = ((Number) target).floatValue();
            } else {
                String targetStringValue = target.toString();
                result = Float.valueOf(targetStringValue);
            }
            result = hookFloat(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Float value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Float floatFormatOfNullable(String targetName, Object target, float defaultValue) {
        if (isEmpty(target)) {
            return hookFloat(defaultValue);
        }
        Float result = null;
        try {
            if (target instanceof Float) {
                result = (Float) target;
            } else if (target instanceof Number) {
                result = ((Number) target).floatValue();
            } else {
                String targetStringValue = target.toString();
                result = Float.valueOf(targetStringValue);
            }
            result = hookFloat(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Float value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Float floatFormat(Object target, float defaultValue, String errorMessage) {
        if (isEmpty(target)) {
            return hookFloat(defaultValue);
        }
        Float result = null;
        try {
            if (target instanceof Float) {
                result = (Float) target;
            } else if (target instanceof Number) {
                result = ((Number) target).floatValue();
            } else {
                String targetStringValue = target.toString();
                result = Float.valueOf(targetStringValue);
            }
            result = hookFloat(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Float floatFormat(String targetName, Object target, float min, float max) {
        Float result = null;
        if (isEmpty(target)) {
            return hookFloat(null);
        }
        try {
            if (target instanceof Float) {
                result = (Float) target;
            } else if (target instanceof Number) {
                result = ((Number) target).floatValue();
            } else {
                String targetStringValue = target.toString();
                result = Float.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        min,
                        max,
                        target.toString()
                        ),
                        null);
            }
            result = hookFloat(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Float value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Float floatFormat(Object target, float min, float max, String errorMessage) {
        Float result = null;
        if (isEmpty(target)) {
            return hookFloat(null);
        }
        try {
            if (target instanceof Float) {
                result = (Float) target;
            } else if (target instanceof Number) {
                result = ((Number) target).floatValue();
            } else {
                String targetStringValue = target.toString();
                result = Float.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookFloat(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Float floatFormatNotEmpty(String targetName, Object target) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        Float result = null;
        try {
            if (target instanceof Float) {
                result = (Float) target;
            } else if (target instanceof Number) {
                result = ((Number) target).floatValue();
            } else {
                String targetStringValue = target.toString();
                result = Float.valueOf(targetStringValue);
            }
            result = hookFloat(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Float value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Float floatFormatNotEmpty(Object target, String errorMessage) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        Float result = null;
        try {
            if (target instanceof Float) {
                result = (Float) target;
            } else if (target instanceof Number) {
                result = ((Number) target).floatValue();
            } else {
                String targetStringValue = target.toString();
                result = Float.valueOf(targetStringValue);
            }
            result = hookFloat(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Float floatFormatNotEmpty(String targetName, Object target, float min, float max) {
        Float result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        try {
            if (target instanceof Float) {
                result = (Float) target;
            } else if (target instanceof Number) {
                result = ((Number) target).floatValue();
            } else {
                String targetStringValue = target.toString();
                result = Float.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        min,
                        max,
                        target.toString()
                        ),
                        null);
            }
            result = hookFloat(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Float value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Float floatFormatNotEmpty(Object target, float min, float max, String errorMessage) {
        Float result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        try {
            if (target instanceof Float) {
                result = (Float) target;
            } else if (target instanceof Number) {
                result = ((Number) target).floatValue();
            } else {
                String targetStringValue = target.toString();
                result = Float.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookFloat(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Double doubleFormat(String targetName, Object target) {
        if (isEmpty(target)) {
            return hookDouble(null);
        }
        Double result = null;
        try {
            if (target instanceof Double) {
                result = (Double) target;
            } else if (target instanceof Number) {
                result = ((Number) target).doubleValue();
            } else {
                String targetStringValue = target.toString();
                result = Double.valueOf(targetStringValue);
            }
            result = hookDouble(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Double value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Double doubleFormatOfNullable(String targetName, Object target, double defaultValue) {
        if (isEmpty(target)) {
            return hookDouble(defaultValue);
        }
        Double result = null;
        try {
            if (target instanceof Double) {
                result = (Double) target;
            } else if (target instanceof Number) {
                result = ((Number) target).doubleValue();
            } else {
                String targetStringValue = target.toString();
                result = Double.valueOf(targetStringValue);
            }
            result = hookDouble(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Double value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Double doubleFormatOfNullable(Object target, double defaultValue, String errorMessage) {
        if (isEmpty(target)) {
            return hookDouble(defaultValue);
        }
        Double result = null;
        try {
            if (target instanceof Double) {
                result = (Double) target;
            } else if (target instanceof Number) {
                result = ((Number) target).doubleValue();
            } else {
                String targetStringValue = target.toString();
                result = Double.valueOf(targetStringValue);
            }
            result = hookDouble(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Double doubleFormat(String targetName, Object target, double min, double max) {
        Double result = null;
        if (isEmpty(target)) {
            return hookDouble(null);
        }
        try {
            if (target instanceof Double) {
                result = (Double) target;
            } else if (target instanceof Number) {
                result = ((Number) target).doubleValue();
            } else {
                String targetStringValue = target.toString();
                result = Double.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        min,
                        max,
                        target.toString()
                        ),
                        null);
            }
            result = hookDouble(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Double value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Double doubleFormat(Object target, double min, double max, String errorMessage) {
        Double result = null;
        if (isEmpty(target)) {
            return hookDouble(null);
        }
        try {
            if (target instanceof Double) {
                result = (Double) target;
            } else if (target instanceof Number) {
                result = ((Number) target).doubleValue();
            } else {
                String targetStringValue = target.toString();
                result = Double.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookDouble(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Double doubleFormatNotEmpty(String targetName, Object target) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        Double result = null;
        try {
            if (target instanceof Double) {
                result = (Double) target;
            } else if (target instanceof Number) {
                result = ((Number) target).doubleValue();
            } else {
                String targetStringValue = target.toString();
                result = Double.valueOf(targetStringValue);
            }
            result = hookDouble(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Double value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Double doubleFormatNotEmpty(Object target, String errorMessage) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        Double result = null;
        try {
            if (target instanceof Double) {
                result = (Double) target;
            } else if (target instanceof Number) {
                result = ((Number) target).doubleValue();
            } else {
                String targetStringValue = target.toString();
                result = Double.valueOf(targetStringValue);
            }
            result = hookDouble(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Double doubleFormatNotEmpty(String targetName, Object target, double min, double max) {
        Double result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        try {
            if (target instanceof Double) {
                result = (Double) target;
            } else if (target instanceof Number) {
                result = ((Number) target).doubleValue();
            } else {
                String targetStringValue = target.toString();
                result = Double.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        min,
                        max,
                        target.toString()
                        ),
                        null);
            }
            result = hookDouble(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a Double value,not %s.", targetName, target.toString()), e);
        }
        return result;
    }

    @Override
    public Double doubleFormatNotEmpty(Object target, double min, double max, String errorMessage) {
        Double result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        try {
            if (target instanceof Double) {
                result = (Double) target;
            } else if (target instanceof Number) {
                result = ((Number) target).doubleValue();
            } else {
                String targetStringValue = target.toString();
                result = Double.valueOf(targetStringValue);
            }
            if (result < min || result > max) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookDouble(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public BigDecimal bigDecimalFormat(String targetName, String target, int scale, RoundingMode roundingMode) {
        BigDecimal result = null;
        try {
            if (isNotEmpty(target)) {
                result = new BigDecimal(target).setScale(scale, roundingMode);
            }
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a String of Number,not %s.", targetName, target), e);
        }
        return hookBigDecimal(result);
    }

    @Override
    public BigDecimal bigDecimalFormatOfNullable(String targetName, String target, int scale, RoundingMode roundingMode, BigDecimal defaultValue) {
        BigDecimal result = null;
        try {
            if (isEmpty(target)) {
                result = defaultValue;
            } else {
                result = new BigDecimal(target).setScale(scale, roundingMode);
            }
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a String of Number,not %s.", targetName, target), e);
        }
        return hookBigDecimal(result);
    }

    @Override
    public BigDecimal bigDecimalFormatOfNullable(String target, int scale, RoundingMode roundingMode, BigDecimal defaultValue, String errorMessage) {
        BigDecimal result = null;
        try {
            if (isEmpty(target)) {
                result = defaultValue;
            } else {
                result = new BigDecimal(target).setScale(scale, roundingMode);
            }
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return hookBigDecimal(result);
    }

    @Override
    public BigDecimal bigDecimalFormat(String targetName, String target, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max) {
        BigDecimal result = null;
        if (isEmpty(target)) {
            return hookBigDecimal(null);
        }
        try {
            result = new BigDecimal(target).setScale(scale, roundingMode);
            if (result.compareTo(min) < 0 || result.compareTo(max) > 0) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        min.toPlainString(),
                        max.toPlainString(),
                        result.toPlainString()
                        ),
                        null);
            }
            result = hookBigDecimal(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a String of Number,not %s.", targetName, target), e);
        }
        return result;
    }

    @Override
    public BigDecimal bigDecimalFormat(String target, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max, String errorMessage) {
        BigDecimal result = null;
        if (isEmpty(target)) {
            return hookBigDecimal(null);
        }
        try {
            result = new BigDecimal(target).setScale(scale, roundingMode);
            if (result.compareTo(min) < 0 || result.compareTo(max) > 0) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookBigDecimal(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public BigDecimal bigDecimalFormatNotEmpty(String targetName, String target, int scale, RoundingMode roundingMode) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        BigDecimal result = null;
        try {
            result = new BigDecimal(target).setScale(scale, roundingMode);
            result = hookBigDecimal(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a String of Number,not %s.", targetName, target), e);
        }
        return result;
    }

    @Override
    public BigDecimal bigDecimalFormatNotEmpty(String target, int scale, RoundingMode roundingMode, String errorMessage) {
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        BigDecimal result = null;
        try {
            result = new BigDecimal(target).setScale(scale, roundingMode);
            result = hookBigDecimal(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public BigDecimal bigDecimalFormatNotEmpty(String targetName, String target, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max) {
        BigDecimal result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        }
        try {
            result = new BigDecimal(target).setScale(scale, roundingMode);
            if (result.compareTo(min) < 0 || result.compareTo(max) > 0) {
                hookUnexpectedEvent(String.format("Unexpected %s,its value needs to be between %s and %s(closed interval),not %s.",
                        targetName,
                        min.toPlainString(),
                        max.toPlainString(),
                        result.toPlainString()
                        ),
                        null);
            }
            result = hookBigDecimal(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(String.format("Unexpected %s,it should be a String of Number,not %s.", targetName, target), e);
        }
        return result;
    }

    @Override
    public BigDecimal bigDecimalFormatNotEmpty(String target, int scale, RoundingMode roundingMode, BigDecimal min, BigDecimal max, String errorMessage) {
        BigDecimal result = null;
        if (isEmpty(target)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        try {
            result = new BigDecimal(target).setScale(scale, roundingMode);
            if (result.compareTo(min) < 0 || result.compareTo(max) > 0) {
                hookUnexpectedEvent(errorMessage,
                        null);
            }
            result = hookBigDecimal(result);
        } catch (NumberFormatException e) {
            hookUnexpectedEvent(errorMessage, e);
        }
        return result;
    }

    @Override
    public Boolean booleanFormat(String targetName, Object target) {
        Boolean result = null;
        if (target != null) {
            String targetStringValue = target.toString();
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
                    hookUnexpectedEvent(String.format("Unexpected %s,its value should be a Boolean,not %s.", targetName, target.toString()), null);
            }
        }
        return hookBoolean(result);
    }

    @Override
    public Boolean booleanFormatOfNullable(String targetName, Object target, Boolean defaultValue) {
        Boolean result = null;
        if (target == null) {
            result = defaultValue;
        } else {
            String targetStringValue = target.toString();
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
                    hookUnexpectedEvent(String.format("Unexpected %s,its value should be a Boolean,not %s.", targetName, target.toString()), null);
            }
        }
        return hookBoolean(result);
    }

    @Override
    public Boolean booleanFormatOfNullable(Object target, Boolean defaultValue, String errorMessage) {
        Boolean result = null;
        if (target == null) {
            result = defaultValue;
        } else {
            String targetStringValue = target.toString();
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
        }
        return hookBoolean(result);
    }

    @Override
    public Boolean booleanFormatNotEmpty(String targetName, Object target) {
        Boolean result = null;
        if (target == null) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be empty.", targetName), null);
        } else {
            String targetStringValue = target.toString();
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
                    hookUnexpectedEvent(String.format("Unexpected %s,its value should be a Boolean,not %s.", targetName, target.toString()), null);
            }
        }
        return hookBoolean(result);
    }

    @Override
    public Boolean booleanFormatNotEmpty(Object target, String errorMessage) {
        Boolean result = null;
        if (target == null) {
            hookUnexpectedEvent(errorMessage, null);
        } else {
            String targetStringValue = target.toString();
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
        }
        return hookBoolean(result);
    }

    @Override
    public String leftFillString(String target, int totalLength, String fillingItem, StringFormatModeEnum stringFormatModeEnum) {
        objectNotNull("stringFormatModeEnum", stringFormatModeEnum);
        stringNotEmpty("fillingItem", (Object) fillingItem);
        String rowString = getObjectOfNullable("target", target, "");
        if (rowString.length() > totalLength) {
            hookUnexpectedEvent(String.format("Unexpected totalLength,it should be no more than %d.", totalLength), null);
        }
        int remainLength = totalLength - rowString.length();
        int itemLength = fillingItem.length();
        if (remainLength % itemLength != 0) {
            hookUnexpectedEvent(String.format("Unexpected fillingItem,%s cannot be filled to the length of %d by %s.",
                    target,
                    totalLength,
                    fillingItem), null);
        }
        StringBuilder stringBuilder = new StringBuilder();
        int appendCount = remainLength / itemLength;
        for (int i = 0; i < appendCount; i++) {
            stringBuilder.append(fillingItem);
        }
        stringBuilder.append(target);
        String result;
        switch (stringFormatModeEnum) {
            case LOWERCASE:
                result = stringBuilder.toString().toLowerCase();
                break;
            case UPPERCASE:
                result = stringBuilder.toString().toUpperCase();
                break;
            default:
                result = stringBuilder.toString();
        }
        return result;
    }

    @Override
    public String rightFillString(String target, int totalLength, String fillingItem, StringFormatModeEnum stringFormatModeEnum) {
        objectNotNull("stringFormatModeEnum", stringFormatModeEnum);
        stringNotEmpty("fillingItem", (Object) fillingItem);
        String rowString = getObjectOfNullable("target", target, "");
        if (rowString.length() > totalLength) {
            hookUnexpectedEvent(String.format("Unexpected totalLength,it should be no more than %d.", totalLength), null);
        }
        int remainLength = totalLength - rowString.length();
        int itemLength = fillingItem.length();
        if (remainLength % itemLength != 0) {
            hookUnexpectedEvent(String.format("Unexpected fillingItem,%s cannot be filled to the length of %d by %s.",
                    target,
                    totalLength,
                    fillingItem), null);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(target);
        int appendCount = remainLength / itemLength;
        for (int i = 0; i < appendCount; i++) {
            stringBuilder.append(fillingItem);
        }
        String result;
        switch (stringFormatModeEnum) {
            case LOWERCASE:
                result = stringBuilder.toString().toLowerCase();
                break;
            case UPPERCASE:
                result = stringBuilder.toString().toUpperCase();
                break;
            default:
                result = stringBuilder.toString();
        }
        return result;
    }

    @Override
    public String upperCaseFirstLetter(String target) {
        if (target == null) {
            return null;
        }
        if (target.length() < UPPER_CASE_FIRST_LETTER_MIN_LENGTH) {
            return target.toUpperCase();
        }
        return target.substring(0, 1).toUpperCase().concat(target.substring(1));
    }

    @Override
    public String lowerCaseFirstLetter(String target) {
        if (target == null) {
            return null;
        }
        if (target.length() < LOWER_CASE_FIRST_LETTER_MIN_LENGTH) {
            return target.toLowerCase();
        }
        return target.substring(0, 1).toLowerCase().concat(target.substring(1));
    }

    @Override
    public String removeStringFormTail(String target, String string, int time) {
        if (time < 1) {
            hookUnexpectedEvent(String.format("Unexpected time of removeStringFormTail,it should more than 0 but %s.", time), null);
        }
        String result = target;
        if (result != null && string != null) {
            for (int i = 0; i < time; i++) {
                if (result.length() >= string.length()) {
                    int index = result.lastIndexOf(string);
                    if (index > 0 && index == (result.length() - string.length())) {
                        result = result.substring(0, index);
                    }
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public StringBuilder removeStringFormTail(StringBuilder target, String string, int time) {
        if (time < 1) {
            hookUnexpectedEvent(String.format("Unexpected time of removeStringFormTail,it should more than 0 but %s.", time), null);
        }
        if (target != null && string != null) {
            for (int i = 0; i < time; i++) {
                if (target.length() >= string.length()) {
                    int index = target.lastIndexOf(string);
                    if (index > 0 && index == (target.length() - string.length())) {
                        target.delete(index, target.length());
                    }
                }
            }
            return target;
        }
        return null;
    }

    @Override
    public StringBuilder getTextFileSpeContent(StringBuilder source, String startMark, String endMark) {
        if (source == null || source.length() < 1) {
            return null;
        }

        int startIndex = source.indexOf(startMark);
        if (startIndex < 0) {
            return null;
        }

        int endIndex = source.lastIndexOf(endMark);
        if (endIndex < 0 || endIndex <= startIndex) {
            return null;
        }

        StringBuilder result = new StringBuilder();

        try (Scanner scanner = new Scanner(source.toString())) {
            boolean needAppend = false;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (!needAppend && line.contains(startMark)) {
                    needAppend = true;
                    result.append(line);
                    result.append(LINE_FEED);
                } else if (needAppend) {
                    result.append(line);
                    result.append(LINE_FEED);
                    if (line.contains(endMark)) {
                        break;
                    }
                }
            }
        }
        return result;
    }
}