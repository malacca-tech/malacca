package org.malacca.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/8/14
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class SimpleJson {

    public static JSONObject buildSuccessJsonRecords(ResultSet resultSet) throws SQLException {
        return buildResultJson(resultSet, (rs, array) -> {
//            int i = 0;
            while (rs.next()) {
                buildJsonRow(rs, array);
//                i++;
//                if (i > 2000) {
//                    break;
//                }
            }
        });
    }

    public static void buildJsonRow(ResultSet resultSet, JSONArray array) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        JSONObject row = new JSONObject();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            row.put(columnName, ResultSetValue.anyToString(resultSet.getObject(columnName)));
        }
        array.add(row);
    }

    public static JSONObject buildResultJson(ResultSet resultSet, ResultSetCallback callback) throws SQLException {
        JSONObject json = new JSONObject();
        json.put("code", 1);
        JSONArray array = new JSONArray();
        callback.callback(resultSet, array);
        json.put("data", array);
        return json;
    }

    public static JSONObject buildResultStringJson(String  data) throws SQLException {
        JSONObject json = new JSONObject();
        json.put("code", 1);
        json.put("data", data);
        return json;
    }

    public interface ResultSetCallback {
        void callback(ResultSet resultSet, JSONArray array) throws SQLException;
    }


}
