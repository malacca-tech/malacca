package org.malacca.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.component.Component;
import org.malacca.component.SqlOutComponent;
import org.malacca.definition.ComponentDefinition;
import org.malacca.support.parser.ComponentParser;

import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/2
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class SqlOutComponentParser implements ComponentParser {

    private static final String URL_KEY = "url";
    public static final String DRIVER_CLASS_NAME_KEY = "driverClassName";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String SQL_KEY = "sql";

    @Override
    public Component createInstance(ComponentDefinition definition) {
        SqlOutComponent component = new SqlOutComponent(definition.getId(), definition.getName());
        component.setStatus(definition.isStatus());
        component.setEnv(definition.getEnv());
        Map<String, Object> params = definition.getParams();
        setUrl(component, params.get(URL_KEY));
        setDriverClassName(component, params.get(DRIVER_CLASS_NAME_KEY));
        setUsername(component, params.get(USERNAME_KEY));
        setPassword(component, params.get(PASSWORD_KEY));
        setSql(component, params.get(SQL_KEY));
        return component;
    }

    private void setUrl(SqlOutComponent component, Object url) {
        Assert.isInstanceOf(String.class, url, "the url is not a string!");
        String urlStr = (String) url;
        Assert.notBlank(urlStr, "url cannot be blank");
        component.setUrl(urlStr);
    }

    private void setDriverClassName(SqlOutComponent component, Object driverClassName) {
        Assert.isInstanceOf(String.class, driverClassName, "the driverClassName is not a string!");
        String driverClassNameStr = (String) driverClassName;
        Assert.notBlank(driverClassNameStr, "driverClassName cannot be blank");
        component.setDriverClassName(driverClassNameStr);
    }

    private void setUsername(SqlOutComponent component, Object username) {
        Assert.isInstanceOf(String.class, username, "the username is not a string!");
        String usernameStr = (String) username;
        Assert.notBlank(usernameStr, "username cannot be blank");
        component.setUsername(usernameStr);
    }

    private void setPassword(SqlOutComponent component, Object password) {
        Assert.isInstanceOf(String.class, password, "the password is not a string!");
        String passwordStr = (String) password;
        Assert.notBlank(passwordStr, "password cannot be blank");
        component.setPassword(passwordStr);
    }

    private void setSql(SqlOutComponent component, Object sql) {
        Assert.isInstanceOf(String.class, sql, "the sql is not a string!");
        String sqlStr = (String) sql;
        Assert.notBlank(sqlStr, "sql cannot be blank");
        component.setSql(sqlStr);
    }
}
