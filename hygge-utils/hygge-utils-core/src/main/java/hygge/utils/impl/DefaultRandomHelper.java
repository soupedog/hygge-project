package hygge.utils.impl;


import hygge.utils.base.BaseRandomHelper;

/**
 * 默认的 RandomHelper 实现类
 *
 * @author Xavier
 * @date 2022/7/10
 * @since 1.0
 */
public class DefaultRandomHelper extends BaseRandomHelper {
    @Override
    protected char hookSingleChar(char singleChar) {
        switch (singleChar) {
            // 数字0被替换成2
            case '0':
                singleChar = '2';
                break;
            // 大写字母 O 被替换成 X
            case 'O':
                singleChar = 'X';
                break;
            // 小写字母 o 被替换成 x
            case 'o':
                singleChar = 'x';
                break;
            default:
                break;
        }
        return singleChar;
    }
}
