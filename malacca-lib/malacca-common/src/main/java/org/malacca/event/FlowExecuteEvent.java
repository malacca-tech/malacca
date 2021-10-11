package org.malacca.event;


import lombok.Data;
import org.malacca.log.LogContext;
import org.malacca.messaging.Message;

/**
 * Description: malacca-ee
 * <p>
 * Created by chensheng on 2021/4/26 15:47
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
@Data
public class FlowExecuteEvent extends MalaccaEvent {
    private Message<?> message;
    private Message<?> originMessage;
    private LogContext logContext;
    private long consumeTime;

    public FlowExecuteEvent() {
    }

    public FlowExecuteEvent(String code, String tips, LogContext logContext, Message<?> message) {
        super(code, tips);
        this.logContext = logContext;
        this.message = message;
    }

    public FlowExecuteEvent(String code, String tips, LogContext logContext, Message<?> message, Message<?> originMessage) {
        super(code, tips);
        this.logContext = logContext;
        this.message = message;
        this.originMessage = originMessage;
    }

    public FlowExecuteEvent(String code, String tips, LogContext logContext, Message<?> message, Message<?> originMessage, long consumeTime) {
        super(code, tips);
        this.logContext = logContext;
        this.message = message;
        this.consumeTime = consumeTime;
        this.originMessage = originMessage;
    }

    public FlowExecuteEvent(String code, String tips, LogContext logContext, Message<?> message, long consumeTime, Exception exception) {
        super(code, tips, exception);
        this.logContext = logContext;
        this.message = message;
        this.consumeTime = consumeTime;
    }

    public FlowExecuteEvent(String code, String tips, LogContext logContext) {
        super(code, tips);
        this.logContext = logContext;
    }

    public FlowExecuteEvent(String code, String tips, LogContext logContext, Exception exception) {
        super(code, tips, exception);
        this.logContext = logContext;
    }
}
