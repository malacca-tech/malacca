package org.malacca.entry.holder;

import org.malacca.entry.Poller;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: http 统一入口 实现
 * </p>
 * <p>
 * Author :chensheng 2020/2/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public abstract class AbstractPollerEntryHolder implements EntryHolder<Poller> {
    /**
     * http Entry缓存
     */
    private Map<String, Poller> pollerEntryMap;

    @Override
    public void loadEntry(String id, Poller entry) {
        getHttpEntryMap().put(id, entry);
    }

    @Override
    public void unloadEntry(String id, Poller entry) {
        getHttpEntryMap().remove(id, entry);
    }

    public Map<String, Poller> getHttpEntryMap() {
        if (pollerEntryMap == null) {
            this.pollerEntryMap = new HashMap<>(16);
        }
        return this.pollerEntryMap;
    }
}
