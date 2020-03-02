package org.malacca.event;

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
public interface EventListener extends java.util.EventListener {
    /**
     * 处理事件
     *
     * @param event
     */
    void onEvent(Event event);
}
