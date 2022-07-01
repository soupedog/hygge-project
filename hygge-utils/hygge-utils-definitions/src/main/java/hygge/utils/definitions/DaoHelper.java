package hygge.utils.definitions;


import hygge.utils.HyggeUtil;
import hygge.utils.bo.ColumnInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * DAO 工具类
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface DaoHelper extends HyggeUtil {
    /**
     * 根据属性信息，筛选出有效属性并生成新的 Map 结构
     *
     * @param rawData             原始数据
     * @param checkInfoCollection 属性信息
     * @return 筛选过后的新 Map,不会为 null
     */
    HashMap<String, Object> filterOutTheFinalAttributes(Map<String, ?> rawData, Collection<ColumnInfo> checkInfoCollection);

    /**
     * 根据属性信息，筛选出有效属性并生成新的 Map 结构
     *
     * @param rawData             原始数据
     * @param checkInfoCollection 属性信息
     * @param successCallBack     成功筛选出有效属性时的回调函数
     * @return 筛选过后的新 Map,不会为 null
     */
    HashMap<String, Object> filterOutTheFinalAttributes(Map<String, ?> rawData, Collection<ColumnInfo> checkInfoCollection, UnaryOperator<HashMap<String, ?>> successCallBack);
}
