package org.malacca.event.entity.system;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/4
 * </p>
 * <p>
 * Department :
 * </p>
 */
// TODO: 2020/3/8 轮询任务事件派发记录任务状态，应该在哪里添加 是不是这些轮询任务应该有在单独使用的组件比如企业版组件使用这些记录功能
public class PollerStartedEvent extends ApplicationContextEvent {
    public PollerStartedEvent(ApplicationContext source) {
        super(source);
    }
}
