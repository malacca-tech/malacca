package org.malacca.event;

import java.util.EventListener;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/25
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface MalaccaEventListener extends EventListener {
    /**
     * 处理事件
     *
     * @param event
     */
    void onEvent(Event event);
}
