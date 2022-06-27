package hygge.utils.base;


import hygge.utils.definitions.ParameterHelper;

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
        return hookString(target.toString());
    }

    @Override
    public String string(String targetName, Object target, int minLength, int maxLength) {
        String resultTemp = null;
        if (isEmpty(target)) {
            if (0 < minLength) {
                hookUnexpectedEvent(
                        unexpectedStringLength(target == null ? null : target.toString(),
                                targetName,
                                minLength,
                                maxLength),
                        null);
            }
        } else {
            resultTemp = target.toString();
            int resultLength = resultTemp.length();

            if (resultLength < minLength || resultLength > maxLength) {
                hookUnexpectedEvent(unexpectedStringLength(resultTemp, targetName, minLength, maxLength),
                        null);
            }
        }
        return hookString(resultTemp);
    }

    @Override
    public String string(Object target, int minLength, int maxLength, String errorMessage) {
        String resultTemp = null;
        if (isEmpty(target)) {
            if (0 < minLength) {
                hookUnexpectedEvent(errorMessage, null);
            }
        } else {
            resultTemp = target.toString();
            int resultLength = resultTemp.length();

            if (resultLength < minLength || resultLength > maxLength) {
                hookUnexpectedEvent(errorMessage, null);
            }
        }
        return hookString(resultTemp);
    }

    @Override
    public String stringNotEmpty(String targetName, Object target) {
        notEmpty(targetName, target);

        String resultTemp = target.toString();
        return hookString(resultTemp);
    }

    @Override
    public String stringNotEmpty(Object target, String errorMessage) {
        notEmpty(target, errorMessage);

        String resultTemp = target.toString();
        return hookString(resultTemp);
    }

    @Override
    public String stringNotEmpty(String targetName, Object target, int minLength, int maxLength) {
        notEmpty(targetName, target);

        String resultTemp = target.toString();
        int resultLength = resultTemp.length();

        if (resultLength < minLength || resultLength > maxLength) {
            hookUnexpectedEvent(unexpectedStringLength(resultTemp, targetName, minLength, maxLength),
                    null);
        }
        return hookString(resultTemp);
    }

    @Override
    public String stringNotEmpty(Object target, int minLength, int maxLength, String errorMessage) {
        notEmpty(target, errorMessage);

        String resultTemp = target.toString();
        int resultTempLength = resultTemp.length();

        if (resultTempLength < minLength || resultTempLength > maxLength) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookString(resultTemp);
    }

    @Override
    public Byte byteFormat(String targetName, Object target) {
        Byte result = parseByte(targetName, target);
        return hookByte(result);
    }

    @Override
    public byte byteFormatOfNullable(String targetName, Object target, byte defaultValue) {
        Byte result = parseByte(targetName, target);

        if (result == null) {
            result = defaultValue;
        }
        return hookByte(result);
    }

    @Override
    public byte byteFormatOfNullable(Object target, byte defaultValue, String errorMessage) {
        Byte result = parseByte(target, errorMessage);

        if (result == null) {
            result = defaultValue;
        }
        return hookByte(result);
    }

    @Override
    public Byte byteFormat(String targetName, Object target, byte min, byte max) {
        Byte result = parseByte(targetName, target);

        boolean notNull = result != null;
        if (notNull && (result < min || result > max)) {
            hookUnexpectedEvent(unexpectedNumberValue(result, targetName, Byte.class.getSimpleName(), min, max), null);
        }
        return hookByte(result);
    }

    @Override
    public Byte byteFormat(Object target, byte min, byte max, String errorMessage) {
        Byte result = parseByte(target, errorMessage);

        boolean notNull = result != null;
        if (notNull && (result < min || result > max)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookByte(result);
    }

    @Override
    public Byte byteFormatNotEmpty(String targetName, Object target) {
        Byte result = parseByte(targetName, target);

        objectNotNull(targetName, result);

        return hookByte(result);
    }

    @Override
    public Byte byteFormatNotEmpty(Object target, String errorMessage) {
        Byte result = parseByte(target, errorMessage);

        objectNotNull(result, errorMessage);

        return hookByte(result);
    }

    @Override
    public Byte byteFormatNotEmpty(String targetName, Object target, byte min, byte max) {
        Byte result = parseByte(targetName, target);

        objectNotNull(targetName, result);

        if (result < min || result > max) {
            hookUnexpectedEvent(unexpectedNumberValue(result, targetName, Byte.class.getSimpleName(), min, max), null);
        }
        return hookByte(result);
    }

    @Override
    public Byte byteFormatNotEmpty(Object target, byte min, byte max, String errorMessage) {
        Byte result = parseByte(target, errorMessage);

        objectNotNull(result, errorMessage);

        if (result < min || result > max) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookByte(result);
    }

    @Override
    public Short shortFormat(String targetName, Object target) {
        Short result = parseShort(targetName, target);
        return hookShort(result);
    }

    @Override
    public short shortFormatOfNullable(String targetName, Object target, short defaultValue) {
        Short result = parseShort(targetName, target);

        if (result == null) {
            result = defaultValue;
        }
        return hookShort(result);
    }

    @Override
    public short shortFormat(Object target, short defaultValue, String errorMessage) {
        Short result = parseShort(target, errorMessage);

        if (result == null) {
            result = defaultValue;
        }
        return hookShort(result);
    }

    @Override
    public Short shortFormat(String targetName, Object target, short min, short max) {
        Short result = parseShort(targetName, target);

        boolean notNull = result != null;
        if (notNull && (result < min || result > max)) {
            hookUnexpectedEvent(unexpectedNumberValue(result, targetName, Short.class.getSimpleName(), min, max), null);
        }
        return hookShort(result);
    }

    @Override
    public Short shortFormat(Object target, short min, short max, String errorMessage) {
        Short result = parseShort(target, errorMessage);

        boolean notNull = result != null;
        if (notNull && (result < min || result > max)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookShort(result);
    }

    @Override
    public Short shortFormatNotEmpty(String targetName, Object target) {
        Short result = parseShort(targetName, target);

        objectNotNull(targetName, result);

        return hookShort(result);
    }

    @Override
    public Short shortFormatNotEmpty(Object target, String errorMessage) {
        Short result = parseShort(target, errorMessage);

        objectNotNull(result, errorMessage);

        return hookShort(result);
    }

    @Override
    public Short shortFormatNotEmpty(String targetName, Object target, short min, short max) {
        Short result = parseShort(targetName, target);

        objectNotNull(targetName, result);

        if (result < min || result > max) {
            hookUnexpectedEvent(unexpectedNumberValue(result, targetName, Short.class.getSimpleName(), min, max), null);
        }
        return hookShort(result);
    }

    @Override
    public Short shortFormatNotEmpty(Object target, short min, short max, String errorMessage) {
        Short result = parseShort(target, errorMessage);

        objectNotNull(result, errorMessage);

        if (result < min || result > max) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookShort(result);
    }

    @Override
    public Integer integerFormat(String targetName, Object target) {
        Integer result = parseInteger(targetName, target);
        return hookInteger(result);
    }

    @Override
    public int integerFormatOfNullable(String targetName, Object target, int defaultValue) {
        Integer result = parseInteger(targetName, target);

        if (result == null) {
            result = defaultValue;
        }
        return hookInteger(result);
    }

    @Override
    public int integerFormat(Object target, int defaultValue, String errorMessage) {
        Integer result = parseInteger(target, errorMessage);

        if (result == null) {
            result = defaultValue;
        }
        return hookInteger(result);
    }

    @Override
    public Integer integerFormat(String targetName, Object target, int min, int max) {
        Integer result = parseInteger(targetName, target);

        boolean notNull = result != null;
        if (notNull && (result < min || result > max)) {
            hookUnexpectedEvent(unexpectedNumberValue(result, targetName, Integer.class.getSimpleName(), min, max), null);
        }
        return hookInteger(result);
    }

    @Override
    public Integer integerFormat(Object target, int min, int max, String errorMessage) {
        Integer result = parseInteger(target, errorMessage);

        boolean notNull = result != null;
        if (notNull && (result < min || result > max)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookInteger(result);
    }

    @Override
    public Integer integerFormatNotEmpty(String targetName, Object target) {
        Integer result = parseInteger(targetName, target);

        objectNotNull(targetName, result);

        return hookInteger(result);
    }

    @Override
    public Integer integerFormatNotEmpty(Object target, String errorMessage) {
        Integer result = parseInteger(target, errorMessage);

        objectNotNull(result, errorMessage);

        return hookInteger(result);
    }

    @Override
    public Integer integerFormatNotEmpty(String targetName, Object target, int min, int max) {
        Integer result = parseInteger(targetName, target);

        objectNotNull(targetName, result);

        if (result < min || result > max) {
            hookUnexpectedEvent(unexpectedNumberValue(result, targetName, Integer.class.getSimpleName(), min, max), null);
        }
        return hookInteger(result);
    }

    @Override
    public Integer integerFormatNotEmpty(Object target, int min, int max, String errorMessage) {
        Integer result = parseInteger(target, errorMessage);

        objectNotNull(result, errorMessage);

        if (result < min || result > max) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookInteger(result);
    }

    @Override
    public Long longFormat(String targetName, Object target) {
        Long result = parseLong(targetName, target);
        return hookLong(result);
    }

    @Override
    public long longFormatOfNullable(String targetName, Object target, long defaultValue) {
        Long result = parseLong(targetName, target);

        if (result == null) {
            result = defaultValue;
        }
        return hookLong(result);
    }

    @Override
    public long longFormat(Object target, long defaultValue, String errorMessage) {
        Long result = parseLong(target, errorMessage);

        if (result == null) {
            result = defaultValue;
        }
        return hookLong(result);
    }

    @Override
    public Long longFormat(String targetName, Object target, long min, long max) {
        Long result = parseLong(targetName, target);

        boolean notNull = result != null;
        if (notNull && (result < min || result > max)) {
            hookUnexpectedEvent(unexpectedNumberValue(result, targetName, Long.class.getSimpleName(), min, max), null);
        }
        return hookLong(result);
    }

    @Override
    public Long longFormat(Object target, long min, long max, String errorMessage) {
        Long result = parseLong(target, errorMessage);

        boolean notNull = result != null;
        if (notNull && (result < min || result > max)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookLong(result);
    }

    @Override
    public Long longFormatNotEmpty(String targetName, Object target) {
        Long result = parseLong(targetName, target);

        objectNotNull(targetName, result);

        return hookLong(result);
    }

    @Override
    public Long longFormatNotEmpty(Object target, String errorMessage) {
        Long result = parseLong(target, errorMessage);

        objectNotNull(result, errorMessage);

        return hookLong(result);
    }

    @Override
    public Long longFormatNotEmpty(String targetName, Object target, long min, long max) {
        Long result = parseLong(targetName, target);

        objectNotNull(targetName, result);

        if (result < min || result > max) {
            hookUnexpectedEvent(unexpectedNumberValue(result, targetName, Long.class.getSimpleName(), min, max), null);
        }
        return hookLong(result);
    }

    @Override
    public Long longFormatNotEmpty(Object target, long min, long max, String errorMessage) {
        Long result = parseLong(target, errorMessage);

        objectNotNull(result, errorMessage);

        if (result < min || result > max) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookLong(result);
    }

    @Override
    public Float floatFormat(String targetName, Object target) {
        Float result = parseFloat(targetName, target);
        return hookFloat(result);
    }

    @Override
    public float floatFormatOfNullable(String targetName, Object target, float defaultValue) {
        Float result = parseFloat(targetName, target);

        if (result == null) {
            result = defaultValue;
        }
        return hookFloat(result);
    }

    @Override
    public float floatFormat(Object target, float defaultValue, String errorMessage) {
        Float result = parseFloat(target, errorMessage);

        if (result == null) {
            result = defaultValue;
        }
        return hookFloat(result);
    }

    @Override
    public Float floatFormat(String targetName, Object target, float min, float max) {
        Float result = parseFloat(targetName, target);

        boolean notNull = result != null;
        if (notNull && (result < min || result > max)) {
            hookUnexpectedEvent(unexpectedNumberValue(result, targetName, Float.class.getSimpleName(), min, max), null);
        }
        return hookFloat(result);
    }

    @Override
    public Float floatFormat(Object target, float min, float max, String errorMessage) {
        Float result = parseFloat(target, errorMessage);

        boolean notNull = result != null;
        if (notNull && (result < min || result > max)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookFloat(result);
    }

    @Override
    public Float floatFormatNotEmpty(String targetName, Object target) {
        Float result = parseFloat(targetName, target);

        objectNotNull(targetName, result);

        return hookFloat(result);
    }

    @Override
    public Float floatFormatNotEmpty(Object target, String errorMessage) {
        Float result = parseFloat(target, errorMessage);

        objectNotNull(result, errorMessage);

        return hookFloat(result);
    }

    @Override
    public Float floatFormatNotEmpty(String targetName, Object target, float min, float max) {
        Float result = parseFloat(targetName, target);

        objectNotNull(targetName, result);

        if (result < min || result > max) {
            hookUnexpectedEvent(unexpectedNumberValue(result, targetName, Float.class.getSimpleName(), min, max), null);
        }
        return hookFloat(result);
    }

    @Override
    public Float floatFormatNotEmpty(Object target, float min, float max, String errorMessage) {
        Float result = parseFloat(target, errorMessage);

        objectNotNull(result, errorMessage);

        if (result < min || result > max) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookFloat(result);
    }

    @Override
    public Double doubleFormat(String targetName, Object target) {
        Double result = parseDouble(targetName, target);
        return hookDouble(result);
    }

    @Override
    public double doubleFormatOfNullable(String targetName, Object target, double defaultValue) {
        Double result = parseDouble(targetName, target);

        if (result == null) {
            result = defaultValue;
        }
        return hookDouble(result);
    }

    @Override
    public double doubleFormatOfNullable(Object target, double defaultValue, String errorMessage) {
        Double result = parseDouble(target, errorMessage);

        if (result == null) {
            result = defaultValue;
        }
        return hookDouble(result);
    }

    @Override
    public Double doubleFormat(String targetName, Object target, double min, double max) {
        Double result = parseDouble(targetName, target);

        boolean notNull = result != null;
        if (notNull && (result < min || result > max)) {
            hookUnexpectedEvent(unexpectedNumberValue(result, targetName, Double.class.getSimpleName(), min, max), null);
        }
        return hookDouble(result);
    }

    @Override
    public Double doubleFormat(Object target, double min, double max, String errorMessage) {
        Double result = parseDouble(target, errorMessage);

        boolean notNull = result != null;
        if (notNull && (result < min || result > max)) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookDouble(result);
    }

    @Override
    public Double doubleFormatNotEmpty(String targetName, Object target) {
        Double result = parseDouble(targetName, target);

        objectNotNull(targetName, result);

        return hookDouble(result);
    }

    @Override
    public Double doubleFormatNotEmpty(Object target, String errorMessage) {
        Double result = parseDouble(target, errorMessage);

        objectNotNull(result, errorMessage);

        return hookDouble(result);
    }

    @Override
    public Double doubleFormatNotEmpty(String targetName, Object target, double min, double max) {
        Double result = parseDouble(targetName, target);

        objectNotNull(targetName, result);

        if (result < min || result > max) {
            hookUnexpectedEvent(unexpectedNumberValue(result, targetName, Double.class.getSimpleName(), min, max), null);
        }
        return hookDouble(result);
    }

    @Override
    public Double doubleFormatNotEmpty(Object target, double min, double max, String errorMessage) {
        Double result = parseDouble(target, errorMessage);

        objectNotNull(result, errorMessage);

        if (result < min || result > max) {
            hookUnexpectedEvent(errorMessage, null);
        }
        return hookDouble(result);
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
