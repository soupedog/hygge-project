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
