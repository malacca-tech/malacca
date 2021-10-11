package org.malacca.entry;

import com.alibaba.fastjson.JSONObject;
import com.alipay.common.tracer.util.DummyContextUtil;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;
import org.malacca.pool.ThreadPool;
import org.malacca.pool.PollingRunnable;
import org.malacca.support.MessageBuilder;
import org.malacca.utils.JdbcUtils;
import org.malacca.utils.ResultSetValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

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
public class AdvancedSqlPollerEntry extends AbstractAdvancedPollingEntry {

    public static final Logger log = LoggerFactory.getLogger(AdvancedSqlPollerEntry.class);

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private String sql;

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private static final String TYPE = "pollerEntry";

    public AdvancedSqlPollerEntry(String id, String name) {
        super(id, name);
    }

    @Override
    public void execute() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        queryAndPublish(getQuerySql(jdbcTemplate), jdbcTemplate);
    }

    protected String getQuerySql(JdbcTemplate jdbcTemplate) {
        return sql;
    }

    protected Object queryAndPublish(String sql, JdbcTemplate jdbcTemplate) {
        AtomicInteger count = new AtomicInteger(0);
        Object result = jdbcTemplate.query(sql, resultSet -> {
            RowCallbackHandler rowCallback = new RowCallback();
            while (resultSet.next()) {
                count.getAndIncrement();
                rowCallback.processRow(resultSet);
            }
            return null;
        });
//        log.info(String.format("%s 执行sql：%s，获取结果行数：%s", getServiceId(), sql, count.get()));
        FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                , String.format("%s 执行sql：%s，获取结果行数：%s", getServiceId(), sql, count.get())
                , logContext);
        return result;
    }

    @Override
    public Message doHandleMessage(Message<?> message) {
        try {
            Message<?> resultMessage = getFlowExecutor().execute(getId(), message);
            return resultMessage;
        } catch (Exception e) {
            throw new MessagingException("数据库入口组件执行出现异常", e);
        }
    }

    @Override
    public void setEntryKey() {
        this.entryKey = getId();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    protected DataSource getDataSource() {
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
                ThreadPoolExecutor poolExecutor = ThreadPool.pollingExecutor;
                int poolSize = poolExecutor.getPoolSize();
                BlockingQueue<Runnable> queue = poolExecutor.getQueue();
                int queueSize = queue.size();
//                log.info(String.format("%s正在使用线程池，线程池共%s 个线程,已用%s 个线程，缓存队列共%s个", getServiceId(), poolExecutor.getMaximumPoolSize(), poolSize, queueSize));
                if (poolSize > 190) {
                    FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                            , String.format("%s正在使用线程池，线程池共%s 个线程,已用%s 个线程，缓存队列共%s个", getServiceId(), poolExecutor.getMaximumPoolSize(), poolSize, queueSize), logContext);
                }
            } catch (Exception e) {
            }
            try {
                JSONObject requestBody = new JSONObject();
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    requestBody.put(metaData.getColumnName(i), ResultSetValue.anyToString(resultSet.getObject(i)));
                }
                Message message = MessageBuilder.withPayload(requestBody.toJSONString()).build();
                ThreadPool.execute(new PollingRunnable(getServiceId()) {
                    @Override
                    public void run() {
                        try {
                            DummyContextUtil.createDummyLogContext();
                            handleMessage(message);
                        } catch (Exception e) {
                            log.error("日志链路追踪出现异常", e);
//                            FlowExecutePublisher.publishEvent(FlowExecuteCode.ERROR_SYSTEM, "日志链路追踪出现异常", logContext, e);
                        } finally {
                            try {
                                DummyContextUtil.clearDummyLogContext();
                            } catch (Exception e) {
                                log.error("日志链路追踪关闭出现异常", e);
//                                FlowExecutePublisher.publishEvent(FlowExecuteCode.ERROR_SYSTEM, "日志链路追踪出现异常" , logContext, e);
                            }
                        }
                    }
                });
            } catch (Throwable e) {
//                log.error(getServiceId() + ":" + getId() + "轮询数据异常", e);
                assert e instanceof Exception;
                FlowExecutePublisher.publishEvent(FlowExecuteCode.ERROR_SYSTEM
                        , getServiceId() + ":" + getId() + "轮询数据异常"
                        , logContext, (Exception) e);
                // TODO: 2020/7/1 异常处理 阻塞?
            }
        }
    }
}
