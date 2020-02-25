package org.malacca.event;


import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 监听器注册类
 * </p>
 * <p>
 * Author :chensheng 2020/2/25
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class EventContext {

    private static List<EventListener> list = new ArrayList<EventListener>();

    public static void addListener(EventListener listener) {
        list.add(listener);
    }

    public static void removeListener(EventListener listener) {
        list.remove(listener);
    }

    public static synchronized void dispatch(Event event) {
        for (EventListener listener : list) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                // TODO: 2020/2/25 记录异常
            }
        }
    }
}
