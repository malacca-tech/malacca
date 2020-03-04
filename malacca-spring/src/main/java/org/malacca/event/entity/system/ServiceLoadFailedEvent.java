package org.malacca.event.entity.system;

import org.malacca.service.Service;
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
public class ServiceLoadFailedEvent extends ApplicationContextEvent {

    private Service service;

    public ServiceLoadFailedEvent(ApplicationContext source) {
        super(source);
    }

    public ServiceLoadFailedEvent(ApplicationContext source, Service service) {
        super(source);
        this.service = service;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
