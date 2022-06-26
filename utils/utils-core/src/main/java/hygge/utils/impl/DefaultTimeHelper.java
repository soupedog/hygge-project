package hygge.utils.impl;


import hygge.utils.base.BaseTimeHelper;

import java.time.ZoneOffset;

/**
 * 默认的 TimeHelper 实现类
 *
 * @author Xavier
 * @date 2022/6/26
 * @since 1.0
 */
public class DefaultTimeHelper extends BaseTimeHelper {
    @Override
    public void initZoneOffset() {
        // 默认 Asia/Shanghai 东八区
        defaultZoneOffset = ZoneOffset.of("+8");
    }
}
