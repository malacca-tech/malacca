package org.malacca.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.definition.EntryDefinition;
import org.malacca.entry.Entry;
import org.malacca.entry.SqlPollerEntry;
import org.malacca.support.parser.EntryParser;

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
public class SqlEntryParser implements EntryParser {

    private static final String URL_KEY = "url";
    public static final String DRIVER_CLASS_NAME_KEY = "driverClassName";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String SQL_KEY = "sql";
    public static final String CRON_KEY = "cron";

    @Override
    public Entry createInstance(EntryDefinition definition) {
        SqlPollerEntry entry = new SqlPollerEntry(definition.getId(), definition.getName());
        entry.setStatus(definition.isStatus());
        entry.setEnv(definition.getEnv());
        Map<String, Object> params = definition.getParams();
        setUrl(entry, params.get(URL_KEY));
        setDriverClassName(entry, params.get(DRIVER_CLASS_NAME_KEY));
        setUsername(entry, params.get(USERNAME_KEY));
        setPassword(entry, params.get(PASSWORD_KEY));
        setSql(entry, params.get(SQL_KEY));
        setCron(entry, params.get(CRON_KEY));
        return entry;
    }

    private void setUrl(SqlPollerEntry entry, Object url) {
        Assert.isInstanceOf(String.class, url, "the url is not a string!");
        String urlStr = (String) url;
        Assert.notBlank(urlStr, "url cannot be blank");
        entry.setUrl(urlStr);
    }

    private void setDriverClassName(SqlPollerEntry entry, Object driverClassName) {
        Assert.isInstanceOf(String.class, driverClassName, "the driverClassName is not a string!");
        String driverClassNameStr = (String) driverClassName;
        Assert.notBlank(driverClassNameStr, "driverClassName cannot be blank");
        entry.setDriverClassName(driverClassNameStr);
    }

    private void setUsername(SqlPollerEntry entry, Object username) {
        Assert.isInstanceOf(String.class, username, "the username is not a string!");
        String usernameStr = (String) username;
        Assert.notBlank(usernameStr, "username cannot be blank");
        entry.setUsername(usernameStr);
    }

    private void setPassword(SqlPollerEntry entry, Object password) {
        Assert.isInstanceOf(String.class, password, "the password is not a string!");
        String passwordStr = (String) password;
        Assert.notBlank(passwordStr, "password cannot be blank");
        entry.setPassword(passwordStr);
    }

    private void setSql(SqlPollerEntry entry, Object sql) {
        Assert.isInstanceOf(String.class, sql, "the sql is not a string!");
        String sqlStr = (String) sql;
        Assert.notBlank(sqlStr, "sql cannot be blank");
        entry.setSql(sqlStr);
    }

    private void setCron(SqlPollerEntry entry, Object cron) {
        Assert.isInstanceOf(String.class, cron, "the cron is not a string!");
        String cronStr = (String) cron;
        Assert.notBlank(cronStr, "cron cannot be blank");
        entry.setCron(cronStr);
    }
}
