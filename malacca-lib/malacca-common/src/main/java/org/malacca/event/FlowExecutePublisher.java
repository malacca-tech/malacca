package org.malacca.event;

import org.greenrobot.eventbus.EventBus;
import org.malacca.log.LogContext;
import org.malacca.messaging.Message;

/**
 * Description: malacca-ee
 * <p>
 * Created by chensheng on 2021/4/26 17:00
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
public class FlowExecutePublisher {


    public static void publishEvent(String code, String tips, LogContext logContext, Message<?> message) {
        EventBus.getDefault().post(new FlowExecuteEvent(code, tips, logContext, message));
    }

    public static void publishEvent(String code, String tips, LogContext logContext, Message<?> message, Message<?> originMessage, long consumeTime) {
        EventBus.getDefault().post(new FlowExecuteEvent(code, tips, logContext, message, originMessage, consumeTime));
    }

    public static void publishEvent(String code, String tips, LogContext logContext, Message<?> message, long consumeTime, Exception exception) {
        EventBus.getDefault().post(new FlowExecuteEvent(code, tips, logContext, message, consumeTime, exception));
    }

    public static void publishEvent(String code, String tips, LogContext logContext) {
        EventBus.getDefault().post(new FlowExecuteEvent(code, tips, logContext));
    }

    public static void publishEvent(String code, String tips, LogContext logContext, Exception exception) {
        EventBus.getDefault().post(new FlowExecuteEvent(code, tips, logContext, exception));
    }
}
