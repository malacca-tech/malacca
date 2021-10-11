package org.malacca.component;

import cn.hutool.core.util.StrUtil;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.MessagingException;
import org.malacca.exception.SqlMessageHandlerException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.support.helper.MessageFreeMarker;
import org.malacca.utils.JdbcUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Arrays;

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
public class AdvancedSqlOutComponent extends AbstractAdvancedComponent {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private String sql;

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private TransactionTemplate transactionTemplate;

    public AdvancedSqlOutComponent(String id, String name) {
        super(id, name);
    }

    @Override
    public Message doHandleMessage(Message<?> message) throws MessagingException {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        TransactionTemplate transactionTemplate = getTransactionTemplate();
        try {
            MessageFreeMarker messageFreeMarker = new MessageFreeMarker(message);
            String sqlStr = messageFreeMarker.parseExpression(sql);
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                    , String.format("%s-FreeMarker配置:\n%s, \n解析SQL: %s \n==>\n 执行SQL:%s", logContext.getServiceId(), messageFreeMarker.getMessageMap(), sql, sqlStr)
                    , logContext);
            // 多条批处理
            String[] sqls = sqlStr.split(";");
            SqlResult result = transactionTemplate.execute(transactionStatus -> {
                SqlResult sqlResult = new SqlResult(true);
                try {
                    for (String sql : sqls) {
                        sql = sql.trim();
                        if (StrUtil.isNotBlank(sql)) {
                            jdbcTemplate.execute(sql);
                        }
                    }
                } catch (Throwable e) {
                    sqlResult.setE(e);
                    sqlResult.setSuccess(false);
                    transactionStatus.setRollbackOnly();
                }
                return sqlResult;
            });

            if (result.isSuccess) {
                Message resultMessage = MessageBuilder.success().copyContext(message.getContext()).setMessageContext(message.getMessageContext()).build();
                return resultMessage;
            } else {
                throw new SqlMessageHandlerException("数据库出口组件执行出现异常：" + result.getE());
            }
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new SqlMessageHandlerException("数据库出口组件执行出现异常", e);
        }
    }

    private DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = JdbcUtils.getDataSource(driverClassName, url, username, password);
        }
        return dataSource;
    }

    protected JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = JdbcUtils.getTemplate(driverClassName, url, username, password);
        }
        return jdbcTemplate;
    }

    @Override
    public String getType() {
        return "db";
    }

    public TransactionTemplate getTransactionTemplate() {
        if (transactionTemplate == null) {
            transactionTemplate = JdbcUtils.getTransactionTemplate(driverClassName, url, username, password);
        }
        return transactionTemplate;
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

    class SqlResult {
        boolean isSuccess = true;
        Throwable e;

        public SqlResult() {
        }

        public SqlResult(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public SqlResult(boolean isSuccess, Throwable e) {
            this.isSuccess = isSuccess;
            this.e = e;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public Throwable getE() {
            return e;
        }

        public void setE(Throwable e) {
            this.e = e;
        }
    }

}
