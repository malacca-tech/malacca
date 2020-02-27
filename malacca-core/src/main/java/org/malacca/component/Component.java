package org.malacca.component;

import org.malacca.messaging.Message;
import org.malacca.exception.MessagingException;
import org.malacca.service.Listener;

public interface Component {
    /**
     * 统一处理message入口
     *
     * @param message
     * @throws MessagingException
     */
    Message handleMessage(Message<?> message) throws MessagingException;

    /**
     * 获取组件id
     * @return
     */
    String getId();

    /**
     * 获取
     * @return
     */
    String getType();
}
