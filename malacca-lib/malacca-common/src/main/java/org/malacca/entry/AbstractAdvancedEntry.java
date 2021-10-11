package org.malacca.entry;

import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;
import org.malacca.app.AppOwnerComponent;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.MessagingException;
import org.malacca.log.LogComponent;
import org.malacca.log.LogContext;
import org.malacca.log.LogType;
import org.malacca.messaging.Message;
import org.malacca.metric.MetricComponent;
import org.malacca.metric.MetricHelper;
import org.slf4j.Logger;

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
public abstract class AbstractAdvancedEntry extends AbstractEntry implements AppOwnerComponent, LogComponent, MetricComponent {

    protected String app;
    protected LogContext logContext;
    protected Logger logger;
    protected MetricHelper metricHelper;
    protected Logger indexLogger;

    public AbstractAdvancedEntry(String id, String name) {
        super(id, name);
        setMetricHelper(new MetricHelper());
    }

    @Override
    public void setApp(String app) {
        this.app = app;
    }

    @Override
    public void setLogContext(LogContext context) {
        this.logContext = context;
    }

    public String getApp() {
        return app;
    }

    public LogContext getLogContext() {
        return logContext;
    }

    public Logger getLogger() {
        return logger;
    }

    public Logger getIndexLogger() {
        return indexLogger;
    }

    public void setIndexLogger(Logger indexLogger) {
        this.indexLogger = indexLogger;
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void setMetricHelper(MetricHelper metricHelper) {
        this.metricHelper = metricHelper;
    }

    // TODO: 2021/3/10 日志发送事件 （统一处理）
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
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_REQUEST_ENTRY
                    , type + "组件请求消息"
                    , logContext
                    , message);
            resultMessage = doHandleMessage(message);
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_RESPONSE_ENTRY
                    , type + "组件返回消息"
                    , logContext
                    , resultMessage
                    , message
                    , System.currentTimeMillis() - startTime);
        } catch (MessagingException me) {
            FlowExecutePublisher.publishEvent(FlowExecuteCode.ERROR_RESPONSE_ENTRY
                    , type + "组件出现异常"
                    , logContext
                    , message
                    , System.currentTimeMillis() - startTime
                    , me);
            throw me;
        } catch (Exception e) {
            FlowExecutePublisher.publishEvent(FlowExecuteCode.ERROR_RESPONSE_ENTRY
                    , type + "组件出现异常"
                    , logContext
                    , message
                    , System.currentTimeMillis() - startTime
                    , e);
            throw new MessagingException(type + "组件处理失败", e);
        } finally {
            context.stop();
        }
        return resultMessage;
    }

    protected abstract Message doHandleMessage(Message<?> message) throws MessagingException;
}
