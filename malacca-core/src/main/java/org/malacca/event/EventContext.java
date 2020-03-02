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

    private static List<MalaccaEventListener> list = new ArrayList<MalaccaEventListener>();

    /**
     * 添加 监听器
     * @param listener
     */
    public static void addListener(MalaccaEventListener listener) {
        list.add(listener);
    }

    /**
     * 移除监听器
     * @param listener
     */
    public static void removeListener(MalaccaEventListener listener) {
        list.remove(listener);
    }

    public static synchronized void dispatch(Event event) {
        for (MalaccaEventListener listener : list) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                // TODO: 2020/2/25 记录异常
            }
        }
    }
}
