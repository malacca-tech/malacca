package org.malacca.entry.hl7;

import org.malacca.entry.AbstractAdvancedEntry;
import org.malacca.log.LogFactoryType;
import org.malacca.log.LogType;
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
public class Hl7Entry extends AbstractAdvancedEntry {

    private String action;

    public Hl7Entry(String id, String name) {
        super(id, name);
    }

    @Override
    public Message doHandleMessage(Message<?> message) {
        try {
            Message<?> resultMessage = getFlowExecutor().execute(getId(), message);
            return resultMessage;
        } catch (Exception e) {
            return MessageBuilder.error("hl7服务处理失败", e).build();
        }
    }

    @Override
    public void setEntryKey() {
        entryKey = action;
    }

    @Override
    public String getType() {
        return "hl7Entry";
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
