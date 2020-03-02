package org.malacca.entry.holder;

import org.malacca.entry.Poller;
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
public abstract class PollerEntryHolder implements EntryHolder<Poller> {
    /**
     * http Entry缓存
     */
    private Map<String, Poller> PollerEntryMap;

    @Override
    public void loadEntry(String id, Poller entry) {
        getHttpEntryMap().put(id, entry);
    }

    /**
     * 初始化时加载所有的数据库轮询 并生成定时任务
     */
    public abstract void loadPollerEntry();

    public Map<String, Poller> getHttpEntryMap() {
        if (PollerEntryMap == null) {
            this.PollerEntryMap = new HashMap<>();
        }
        return this.PollerEntryMap;
    }
}
