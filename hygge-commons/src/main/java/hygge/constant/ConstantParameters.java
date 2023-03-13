package hygge.constant;

import java.io.File;

/**
 * 一些业务开发需要的常量
 *
 * @author Xavier
 * @date 2023/3/13
 * @since 1.0
 */
public class ConstantParameters {
    private ConstantParameters() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 与宿主系统相适配的换行符
     */
    public static final String LINE_SEPARATOR = System.lineSeparator();
    /**
     * 与宿主系统相适配的目录分隔符
     */
    public static final String FILE_SEPARATOR = File.separator;
}
