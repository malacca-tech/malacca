package org.malacca.entry.register;

import org.malacca.entry.Entry;
import org.malacca.entry.holder.EntryHolder;
import org.malacca.entry.register.EntryRegister;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: entry类型注册抽象类
 * </p>
 * <p>
 * Author :chensheng 2020/2/24
 * </p>
 * <p>
 * Department :
 * </p>
 */
public abstract class AbstractEntryRegister implements EntryRegister {

    /**
     * holder 缓存 创建bean 注入 此holderMap 以供根据type找到对应的holder
     */
    private Map<String, EntryHolder> holderMap = new HashMap<>();

    @Override
    public void loadEntry(Entry entry) {
        EntryHolder entryHolder = holderMap.get(entry.getType());
        // TODO: 2020/2/24 日志
        if (entryHolder != null) {
            entryHolder.loadEntry(entry.getEntryKey(), entry);
        }
    }

    @Override
    public void unloadEntry(Entry entry) {
        EntryHolder entryHolder = holderMap.get(entry.getType());
        // TODO: 2020/2/24 日志
        if (entryHolder != null) {
            entryHolder.unloadEntry(entry.getEntryKey(), entry);
        }
    }

    public Map<String, EntryHolder> getHolderMap() {
        return holderMap;
    }

    public void putHolder(String type, EntryHolder holder) {
        holderMap.put(type, holder);
    }
}
