package org.malacca.event.listener.system;

import org.malacca.event.entity.system.PollerStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

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
@Component
public class PollerStartedEventListener implements ApplicationListener<PollerStartedEvent> {
    @Override
    public void onApplicationEvent(PollerStartedEvent pollerStartedEvent) {

    }
}
