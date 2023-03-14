package hygge.util.base;

import hygge.commons.exception.ParameterRuntimeException;
import hygge.util.UtilCreator;
import hygge.util.bo.ColumnInfo;
import hygge.util.definition.DaoHelper;
import hygge.util.definition.ParameterHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * DAO 工具基类
 *
 * @author Xavier
 * @date 2022/7/11
 * @since 1.0
 */
public abstract class BaseDaoHelper implements DaoHelper {
    protected ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    @Override
    public HashMap<String, Object> filterOutTheFinalColumns(Map<String, Object> rawData, Collection<ColumnInfo> checkInfoCollection) {
        return filterOutTheFinalColumns(rawData, checkInfoCollection, null);
    }

    @Override
    public HashMap<String, Object> filterOutTheFinalColumns(Map<String, Object> rawData, Collection<ColumnInfo> checkInfoCollection, UnaryOperator<HashMap<String, Object>> successCallBack) {
        parameterHelper.objectNotNull("rawData", rawData);
        parameterHelper.notEmpty("checkInfoCollection", checkInfoCollection);

        HashMap<String, Object> result = new HashMap<>(rawData.size());
        // 当前属性名称
        String currentColumnName;
        for (ColumnInfo columnInfo : checkInfoCollection) {
            currentColumnName = columnInfo.getColumnName();

            boolean itemExist = rawData.containsKey(currentColumnName);

            if (itemExist) {
                Object valueTemp = columnInfo.checkAndGetColumn(rawData.get(columnInfo.getColumnName()));
                // 使用属性数据库中名称构造最终键值
                result.put(columnInfo.getColumnDatabaseName(), valueTemp);
            } else if (!columnInfo.isKeyNullable()) {
                // 属性不存在且该属性不许不存在
                throw new ParameterRuntimeException("Unexpected " + currentColumnName + ", it can't be null.");
            }
        }

        if (parameterHelper.isEmpty(result)) {
            StringBuilder msg = new StringBuilder();
            for (ColumnInfo columnInfo : checkInfoCollection) {
                msg.append(columnInfo.getColumnName());
                msg.append(",");
            }
            if (msg.length() > 0) {
                msg.deleteCharAt(msg.length() - 1);
            }
            throw new ParameterRuntimeException(String.format("Insufficient valid attribute values,they can be %s.", msg));
        }
        // 成功回调函数不为空则执行
        if (successCallBack != null) {
            result = successCallBack.apply(result);
        }
        return result;
    }
}
