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
public class PollerStartingEvent extends ApplicationContextEvent {

    public PollerStartingEvent(ApplicationContext source) {
        super(source);
    }
}
