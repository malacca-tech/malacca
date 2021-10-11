package org.malacca.component.entity;

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
public enum DatasourceType {

    ORACLE("oracle.jdbc.OracleDriver"),

    MYSQL("com.mysql.jdbc.Driver"),

    SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver"),

    POSTGRESQL("org.postgresql.Driver"),

    MARIADB("org.mariadb.jdbc.MariaDbDataSource"),

    DB2("com.ibm.db2.jcc.DB2SimpleDataSource"),

    INFORMIX("com.informix.jdbcx.IfxDataSource"),

    FIREBIRD("org.firebirdsql.ds.FBSimpleDataSource"),

    SYBASE("com.sybase.jdbc4.jdbc.SybDataSource");

    private String driverClass;

    DatasourceType(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }
}
