package org.malacca.entry.holder;

import org.malacca.entry.Entry;
import org.malacca.entry.holder.EntryHolder;

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
public abstract class HttpEntryHolder implements EntryHolder<Entry> {
    /**
     * http Entry缓存
     */
    private Map<String, Entry> httpEntryMap;

    @Override
    public void loadEntry(String id, Entry entry) {
        getHttpEntryMap().put(id, entry);
    }

    public Map<String, Entry> getHttpEntryMap() {
        if (httpEntryMap == null) {
            this.httpEntryMap = new HashMap<>();
        }
        return this.httpEntryMap;
    }
}
