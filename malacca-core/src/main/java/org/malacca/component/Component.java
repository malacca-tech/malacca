package org.malacca.component;

import org.malacca.messaging.Message;
import org.malacca.exception.MessagingException;

public interface Component {
    /**
     * 统一处理message入口
     *
     * @param message
     * @throws MessagingException
     */
    Message handleMessage(Message<?> message) throws MessagingException;

    /**
     * 设置组件的id
     *
     * @param componentId
     */
    void setId(String componentId);
}
