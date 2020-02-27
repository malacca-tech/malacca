package org.malacca.service;

import org.malacca.messaging.Message;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/27
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface Listener {

    /**
     * 监听器 监听入口消息
     *
     * @param id      组件id
     * @param message
     */
    Message<?> onfinish(String id, Message<?> message);

}
