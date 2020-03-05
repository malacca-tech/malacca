package org.malacca.entry;

import com.alibaba.fastjson.JSONObject;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.utils.DataSourceUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import javax.sql.DataSource;
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
 * Author :chensheng 2020/3/4
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class SqlPollerEntry extends AbstractPollingEntry {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private String sql;

    private DataSource dataSource;

    private static final String TYPE = "pollerEntry";

    public SqlPollerEntry(String id, String name) {
        super(id, name);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        queryAndPublish(sql, jdbcTemplate);
    }

    private void queryAndPublish(String sql, JdbcTemplate jdbcTemplate) {
        jdbcTemplate.query(sql, resultSet -> {
            RowCallbackHandler rowCallback = new RowCallback();
            while (resultSet.next()) {
                rowCallback.processRow(resultSet);
            }
        });
    }

    @Override
    public Message handleMessage(Message<?> message) {
        Message<?> resultMessage = getFlowExecutor().execute(getId(), message);
        return resultMessage;
    }

    @Override
    public void setEntryKey() {
        this.entryKey = getId();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    private DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = DataSourceUtils.getDataSource(driverClassName, url, username, password);
        }
        return dataSource;
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

    public class RowCallback implements RowCallbackHandler {

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            try {
                JSONObject requestBody = new JSONObject();
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    requestBody.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                Message message = MessageBuilder.withPayload(requestBody.toJSONString()).build();
                handleMessage(message);
            } catch (Throwable e) {
                e.printStackTrace();
                // TODO: 2020/1/14 发送日志
            }
        }
    }
}
