package org.malacca.utils;

import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.OracleType;
import oracle.jdbc.OracleTypes;
import org.malacca.component.entity.DatasourceType;

import javax.sql.DataSource;
import java.sql.Types;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :yangxing 2021/7/15
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class DatasourceUtils {

    public static String getDriver(DataSource dataSource) {
        return ((HikariDataSource) dataSource).getDriverClassName();
    }

    public static boolean isOracle(DataSource dataSource) {
        return DatasourceType.ORACLE.getDriverClass().equals(getDriver(dataSource));
    }

    public static boolean isMysql(DataSource dataSource) {
        return DatasourceType.MYSQL.getDriverClass().equals(getDriver(dataSource));
    }

    public static boolean isSqlserver(DataSource dataSource) {
        return DatasourceType.SQLSERVER.getDriverClass().equals(getDriver(dataSource));
    }

    public static boolean isPostgresql(DataSource dataSource) {
        return DatasourceType.POSTGRESQL.getDriverClass().equals(getDriver(dataSource));
    }

    public static boolean isMaridb(DataSource dataSource) {
        return DatasourceType.MARIADB.getDriverClass().equals(getDriver(dataSource));
    }

    public static boolean isDB2(DataSource dataSource) {
        return DatasourceType.DB2.getDriverClass().equals(getDriver(dataSource));
    }

    public static boolean isInformix(DataSource dataSource) {
        return DatasourceType.INFORMIX.getDriverClass().equals(getDriver(dataSource));
    }

    public static boolean isFirebird(DataSource dataSource) {
        return DatasourceType.FIREBIRD.getDriverClass().equals(getDriver(dataSource));
    }

    public static boolean isSybase(DataSource dataSource) {
        return DatasourceType.SYBASE.getDriverClass().equals(getDriver(dataSource));
    }

    public static int dataType2code(String dataType) {
        switch (dataType.toLowerCase()) {
            case "varchar":
            case "varchar2":
                return Types.VARCHAR;
            case "nvarchar":
                return Types.NVARCHAR;
            case "date":
                return Types.DATE;
            case "timestamp":
                return Types.TIMESTAMP;
            case "numeric":
            case "number":
                return Types.NUMERIC;
            case "int":
            case "integer":
            case "int4":
                return Types.INTEGER;
            case "bigint":
                return Types.BIGINT;
            case "blob":
                return Types.BLOB;
            case "text":
            case "clob":
                return Types.CLOB;
            case "cursor":
                return OracleTypes.CURSOR;
            default:
                throw new RuntimeException("未定义的数据类型:" + dataType);
        }
    }

}
