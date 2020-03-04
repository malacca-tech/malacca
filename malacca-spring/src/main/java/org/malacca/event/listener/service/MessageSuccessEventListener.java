package org.malacca.event.listener.service;

import org.malacca.event.entity.service.MessageSuccessEvent;
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
public class MessageSuccessEventListener implements ApplicationListener<MessageSuccessEvent> {
    @Override
    public void onApplicationEvent(MessageSuccessEvent messageSuccessEvent) {

    }
}
