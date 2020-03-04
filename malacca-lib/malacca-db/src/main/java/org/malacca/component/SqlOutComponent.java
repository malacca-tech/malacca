package org.malacca.component;

import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.utils.DataSourceUtils;
import org.malacca.utils.MessageFreeMarker;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

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
public class SqlOutComponent extends AbstractComponent {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private String sql;

    private DataSource dataSource;

    public SqlOutComponent(String id, String name) {
        super(id, name);
    }

    @Override
    public Message handleMessage(Message<?> message) throws MessagingException {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            MessageFreeMarker messageFreeMarker = new MessageFreeMarker(message, false);
            String sqlStr = messageFreeMarker.parseExpression(sql);
            jdbcTemplate.execute(sqlStr);
            return MessageBuilder.success().build();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: 2020/3/4 exception
        }
        return null;
    }

    private DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = DataSourceUtils.getDataSource(driverClassName, url, username, password);
        }
        return dataSource;
    }

    @Override
    public String getType() {
        return null;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
