package org.malacca.component;

import oracle.jdbc.OracleType;
import org.malacca.component.procedure.*;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.MessagingException;
import org.malacca.exception.SqlMessageHandlerException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.support.helper.MessageFreeMarker;
import org.malacca.utils.DatasourceUtils;
import org.malacca.utils.JdbcUtils;
import org.malacca.utils.SimpleJson;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLType;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
//import org.postgresql.jdbc.PgCallableStatement;

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
public class AdvancedSqlQueryComponent extends AbstractAdvancedComponent {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private String operaType = "SQL"; // PROCEDURE

    private String sql;

    private String procedureName;

    private List<ProcedureParam> paramList = new ArrayList<>();

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    public AdvancedSqlQueryComponent(String id, String name) {
        super(id, name);
    }

    @Override
    public Message doHandleMessage(Message<?> message) throws MessagingException {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        MessageFreeMarker freeMarker = new MessageFreeMarker(message);
        try {
            Object result = "";
            if ("PROCEDURE".equals(operaType)) {
                result = callProcedure(jdbcTemplate, freeMarker);
            } else {
                result = execSql(jdbcTemplate, freeMarker);
            }
            Message resultMessage = MessageBuilder.withPayload(result).copyContext(message.getContext()).build();
            return resultMessage;
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new SqlMessageHandlerException("数据库查询组件执行出现异常", e);
        }
    }

    public String execSql(JdbcTemplate jdbcTemplate, MessageFreeMarker freeMarker) {
        String sqlStr = freeMarker.parseExpression(sql);
        FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                , String.format("%s-FreeMarker配置:\n%s, \n解析SQL: %s \n==>\n 执行SQL:%s", logContext.getServiceId(), freeMarker.getMessageMap(), sql, sqlStr)
                , logContext);
        return jdbcTemplate.query(sqlStr, resultSet -> {
            return SimpleJson.buildSuccessJsonRecords(resultSet).toJSONString();
        });
    }

    private Object callProcedure(JdbcTemplate jdbcTemplate, MessageFreeMarker freeMarker) {
        String sql = buildSql();
        FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                , String.format("%s-执行SQL:%s, 参数列表:%s", logContext.getServiceId(), sql, paramList), logContext);

        DataSource dataSource = jdbcTemplate.getDataSource();
        ProcedureCallableStatementCreator creator = new ProcedureCallableStatementCreator(paramList, dataSource, sql, new DefaultValueHandler(freeMarker));
        creator.setFreeMarker(freeMarker);
        ProcedureCallableStatementCallback callback = new ProcedureCallableStatementCallback(dataSource, paramList, new ProcedureResultSetExtractor());
        return jdbcTemplate.execute(creator, callback);
    }

    private String buildSql() {
        StringJoiner procSql;
        if (DatasourceUtils.isSqlserver(getDataSource())) {
            procSql = new StringJoiner(",", " exec " + procedureName, "");
        } else {
            procSql = new StringJoiner(",", " CALL " + procedureName + "(", ")");
        }
        for (ProcedureParam param : paramList) {
            procSql.add(" ?");
        }
        return procSql.toString();
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

    public void setOperaType(String operaType) {
        this.operaType = operaType;
    }

    public String getOperaType() {
        return operaType;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public List<ProcedureParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<ProcedureParam> paramList) {
        this.paramList = paramList;
    }
}
