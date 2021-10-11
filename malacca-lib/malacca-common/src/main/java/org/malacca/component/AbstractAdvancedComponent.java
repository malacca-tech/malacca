package org.malacca.component;

import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;
import org.malacca.app.AppOwnerComponent;
import org.malacca.custom.ResultCallbackComponent;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.MessagingException;
import org.malacca.log.LogComponent;
import org.malacca.log.LogContext;
import org.malacca.messaging.Message;
import org.malacca.metric.MetricComponent;
import org.malacca.metric.MetricHelper;
import org.malacca.retry.RetryComponent;
import org.malacca.retry.RetryContext;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Date;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public abstract class AbstractAdvancedComponent extends AbstractComponent implements AppOwnerComponent, LogComponent, MetricComponent, ResultCallbackComponent, RetryComponent {

    protected String app;
    protected LogContext logContext;
    protected Logger logger;
    protected MetricHelper metricHelper;
    protected boolean useOriginMessage = false;
    private boolean retry;
    private RetryContext retryContext;
    private RetryTemplate retryTemplate;
    private JdbcTemplate malaccaJdbcTemplate;

    public AbstractAdvancedComponent(String id, String name) {
        super(id, name);
        setMetricHelper(new MetricHelper());
        setRetryContext(new RetryContext());
    }

    @Override
    public void setApp(String app) {
        this.app = app;
    }

    @Override
    public void setLogContext(LogContext context) {
        this.logContext = context;
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String getApp() {
        return app;
    }

    public LogContext getLogContext() {
        return logContext;
    }

    @Override
    public void setMetricHelper(MetricHelper metricHelper) {
        this.metricHelper = metricHelper;
    }

    public boolean isUseOriginMessage() {
        return useOriginMessage;
    }

    @Override
    public void setUseOriginMessage(boolean useOriginMessage) {
        this.useOriginMessage = useOriginMessage;
    }

    @Override
    public Message handleMessage(Message<?> message) throws MessagingException {
        String componentId = getId();
        String serviceId = logContext.getServiceId();
        Meter meter = metricHelper.getMeter(serviceId, componentId);
        Timer timer = metricHelper.getTimer(serviceId, componentId);
        meter.mark();
        Timer.Context context = timer.time();
        Message resultMessage;
        String type = this.getType();
        long startTime = System.currentTimeMillis();
        try {
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_REQUEST_COMPONENT
                    , type + "组件请求消息"
                    , logContext
                    , message);
            if (isRetry()) {
                resultMessage = doWithRetry(message, this);
            } else {
                resultMessage = doHandleMessage(message);
            }
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_RESPONSE_COMPONENT
                    , type + "组件返回消息"
                    , logContext
                    , resultMessage
                    , message
                    , System.currentTimeMillis() - startTime);
        } catch (MessagingException me) {
            FlowExecutePublisher.publishEvent(FlowExecuteCode.ERROR_RESPONSE_COMPONENT
                    , type + "组件出现异常"
                    , logContext
                    , message
                    , System.currentTimeMillis() - startTime
                    , me);
            throw me;
        } catch (Exception e) {
            FlowExecutePublisher.publishEvent(FlowExecuteCode.ERROR_RESPONSE_COMPONENT
                    , type + "组件出现异常"
                    , logContext
                    , message
                    , System.currentTimeMillis() - startTime
                    , e);
            throw new MessagingException(type + "组件处理失败", e);
        } finally {
            context.stop();
        }
        return useOriginMessage ? message : resultMessage;
    }

    @Override
    public void setRetryContext(RetryContext retryContext) {
        this.retryContext = retryContext;
    }

    private RetryTemplate getRetryTemplate() {
        if (retryTemplate == null) {
            RetryTemplate retryTemplate = new RetryTemplate();
            SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(retryContext.getTimes());
            retryTemplate.setRetryPolicy(simpleRetryPolicy);
            this.retryTemplate = retryTemplate;
        }
        return this.retryTemplate;
    }

    private Message<?> doWithRetry(Message<?> message, AbstractAdvancedComponent component) throws Exception {
        //兜底回调
        Message<?> result = getRetryTemplate().execute(new RetryCallback<Message<?>, Exception>() {
                                                           @Override
                                                           public Message<?> doWithRetry(org.springframework.retry.RetryContext retryContext) throws Exception {
                                                               return component.doHandleMessage(message);
                                                           }
                                                       },
                new RecoveryCallback<Message<?>>() {
                    @Override
                    public Message<?> recover(org.springframework.retry.RetryContext retryContext) throws Exception {
                        component.doRecoveryCallbackMessage(message);
                        return message;
                    }
                });
        return result;
    }

    private void doRecoveryCallbackMessage(Message<?> message) {
        if (malaccaJdbcTemplate != null) {
            String sql = "insert into malacca_recovery_message values(?,?,?,?)";
            malaccaJdbcTemplate.update(sql, new Object[]{logContext.getServiceId(), logContext.getAppId(), message.getPayload(), new Date()});
        }
    }

    public JdbcTemplate getMalaccaJdbcTemplate() {
        return malaccaJdbcTemplate;
    }

    public void setMalaccaJdbcTemplate(JdbcTemplate malaccaJdbcTemplate) {
        this.malaccaJdbcTemplate = malaccaJdbcTemplate;
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    protected abstract Message doHandleMessage(Message<?> message) throws MessagingException;
}
