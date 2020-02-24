package org.malacca.entry;

import org.malacca.messaging.Message;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 入口组件
 * </p>
 * <p>
 * Author :chensheng 2020/2/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface Entry {
    /**
     * 处理组装好的消息
     *
     * @param message
     * @return
     */
    Message handleMessage(Message<?> message);

    void setId(String componentId);

    /**
     * 用于区分每个entry的识别 eg: httpEntry -> entryKey= /path  pollerEntry -> entryKey = EntryId
     *
     * @param entryKey
     */
    void setEntryKey(String entryKey);

    String getEntryKey();

    /**
     * 组件类型
     *
     * @param type
     */
    void setType(String type);

    String getType();
}
