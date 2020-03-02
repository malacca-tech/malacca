package org.malacca.exector;

import org.malacca.component.Component;
import org.malacca.flow.Flow;
import org.malacca.messaging.Message;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

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
public interface Executor {
    /**
     * 执行器执行任务
     *
     * @param component
     * @param message
     * @return
     */
    Message<?> execute(String component, Message<?> message);

    void setFlow(Flow flow);

    void setPoolExecutor(ThreadPoolExecutor poolExecutor);

    void setComponentMap(Map<String, Component> componentMap);
}
