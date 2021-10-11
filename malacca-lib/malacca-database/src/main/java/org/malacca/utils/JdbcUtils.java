package org.malacca.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/4
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class JdbcUtils {

    private static Logger log = LoggerFactory.getLogger(JdbcUtils.class);

    private static Map<String, JdbcTemplate> jdbcTemplateMap = new HashMap<>();

    private static Map<String, DataSource> dataSourceMap = new HashMap<>();

    private static Map<String, DataSourceTransactionManager> transactionManagerHashMap = new HashMap<>();

    private static Map<String, TransactionTemplate> transactionTemplateMap = new HashMap<>();

    synchronized public static DataSource getDataSource(String driverClassName, String url, String username, String password) {
        log.info("获取数据库连接池" + url + "," + username);
        DataSource source = dataSourceMap.get(url + username);
        if (source == null) {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(driverClassName);
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(50);
            config.setMinimumIdle(1);
            source = new HikariDataSource(config);
            dataSourceMap.put(url + username, source);
        }
        return source;
    }

    synchronized public static JdbcTemplate getTemplate(String driverClassName, String url, String username, String password) {
        JdbcTemplate jdbcTemplate = jdbcTemplateMap.get(url + username);
        if (jdbcTemplate == null) {
            HikariDataSource dataSource = (HikariDataSource) getDataSource(driverClassName, url, username, password);
            jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplateMap.put(url + username, jdbcTemplate);
        }
        return jdbcTemplate;
    }

    // TODO: 2021/2/25 单一实例是否对事务提交有影响
    public static DataSourceTransactionManager getTransactionManager(String driverClassName, String url, String username, String password) {
        DataSourceTransactionManager dataSourceTransactionManager =
                new DataSourceTransactionManager(getDataSource(driverClassName, url, username, password));
        return dataSourceTransactionManager;
    }

    public static TransactionTemplate getTransactionTemplate(String driverClassName, String url, String username, String password) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(getTransactionManager(driverClassName, url, username, password));
        return transactionTemplate;
    }
}
