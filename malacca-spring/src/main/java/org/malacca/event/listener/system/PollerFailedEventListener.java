package org.malacca.event.listener.system;

import org.malacca.event.entity.system.PollerFailedEvent;
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
public class PollerFailedEventListener implements ApplicationListener<PollerFailedEvent> {
    @Override
    public void onApplicationEvent(PollerFailedEvent pollerFailedEvent) {

    }
}