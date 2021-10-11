package org.malacca.utils;

import oracle.sql.CLOB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ResultSetValue {

    /**
     * 转换成String
     *
     * @param o
     * @return
     */
    public static String anyToString(Object o) {
        String str = "";
        if (o == null) {
            str = "";
        } else if (o.getClass().equals(String.class)) {
            str = (String) o;
        } else if (o.getClass().equals(CLOB.class)) {//CLOB转换成String类型
            CLOB clob = (CLOB) o;
            Reader is = null;
            BufferedReader br = null;
            try {
                is = clob.getCharacterStream();
                br = new BufferedReader(is);
                try {
                    String s = br.readLine();
                    StringBuffer sb = new StringBuffer();
                    while (s != null) {
                        sb.append(s);
                        s = br.readLine();
                    }
                    str = sb.toString();
                } catch (IOException e) {
                    throw new RuntimeException("读取Clob文件失败");
                }
            } catch (SQLException e) {
                throw new RuntimeException("sql执行失败");
            } finally {
                CloseableUtils.close(is);
                CloseableUtils.close(br);
            }
        } else if (o.getClass().equals(BigDecimal.class)) {//BigDecimal转换成String类型
            str = o.toString();
        } else if (o.getClass().equals(Timestamp.class)) {//Timestamp转换成String类型
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = sdf.format((Timestamp) o);
        } else {
            str = o.toString();
        }
        return str;
    }
}
