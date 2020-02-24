package org.malacca.entry.holder;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 统一入口
 * </p>
 * <p>
 * Author :chensheng 2020/2/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface EntryHolder<T> {

    /**
     * 加载入口
     *
     * @param id    serviceId + entryId
     * @param entry httpEntry soapEntry poller
     * @return
     */
    void loadEntry(String id, T entry);

    /**
     * 卸载入口
     *
     * @param id
     * @param entry
     */
    void unloadEntry(String id, T entry);
}
