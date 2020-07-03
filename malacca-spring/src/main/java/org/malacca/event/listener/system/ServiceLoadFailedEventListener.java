package org.malacca.event.listener.system;

import org.malacca.entry.Entry;
import org.malacca.entry.register.EntryRegister;
import org.malacca.event.entity.system.ServiceLoadFailedEvent;
import org.malacca.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: service 失败要执行的东西 eg:卸载入口
 * </p>
 * <p>
 * Author :chensheng 2020/3/4
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class ServiceLoadFailedEventListener implements ApplicationListener<ServiceLoadFailedEvent> {

    private EntryRegister entryRegister;

    @Override
    public void onApplicationEvent(ServiceLoadFailedEvent serviceLoadFailedEvent) {
        Service service = serviceLoadFailedEvent.getService();
        Map<String, Entry> entryMap = service.getEntryMap();
        for (Map.Entry<String, Entry> entry : entryMap.entrySet()) {
            entryRegister.deregisterEntry(entry.getValue());
        }
    }

    public EntryRegister getEntryRegister() {
        return entryRegister;
    }

    public void setEntryRegister(EntryRegister entryRegister) {
        this.entryRegister = entryRegister;
    }
}
