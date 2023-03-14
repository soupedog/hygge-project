package hygge.commons.constants.enums;

/**
 * 字符串取值范围类型枚举
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
@SuppressWarnings("java:S115")
public enum StringCategoryEnum {
    /**
     * 所有数字类型<br/>
     * 0~9
     */
    NUMBER(48, 10),
    /**
     * 所有大写字母<br/>
     * 大写字母A(65)~Z(90)
     */
    A_Z(65, 26),
    /**
     * 所有小写字母<br/>
     * 小写字母A(97)~Z(122)
     */
    a_z(97, 26),
    ;

    /**
     * 首字符 ASCII 码起始序号
     */
    private final Integer asciiStartPoint;
    /**
     * 该类型字符集字符总数
     */
    private final Integer totalSize;

    StringCategoryEnum(Integer asciiStartPoint, Integer totalSize) {
        this.asciiStartPoint = asciiStartPoint;
        this.totalSize = totalSize;
    }

    public Integer getAsciiStartPoint() {
        return asciiStartPoint;
    }

    public Integer getTotalSize() {
        return totalSize;
    }
}
