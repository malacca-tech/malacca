package org.malacca.entry.register;

import org.malacca.entry.Entry;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: entry类型注册类
 * </p>
 * <p>
 * Author :chensheng 2020/2/24
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface EntryRegister {

    /**
     * 注册入口
     *
     * @param entry 入口实例
     */
    void loadEntry(Entry entry);

    void unloadEntry(Entry entry);
}
