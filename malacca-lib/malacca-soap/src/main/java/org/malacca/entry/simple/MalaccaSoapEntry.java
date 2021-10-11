package org.malacca.entry.simple;

import org.malacca.entry.AbstractAdvancedEntry;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/6
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class MalaccaSoapEntry extends AbstractAdvancedEntry {

    public MalaccaSoapEntry(String id, String name) {
        super(id, name);
    }

    @Override
    public Message doHandleMessage(Message<?> message) {
        try {
            Message<?> resultMessage = getFlowExecutor().execute(getId(), message);
            return resultMessage;
        } catch (Exception e) {
            return MessageBuilder.error("webservice服务处理失败", e).build();
        }
    }

    @Override
    public void setEntryKey() {
        entryKey = getServiceId();
    }

    @Override
    public String getType() {
        return "simpleSoapEntry";
    }
}
