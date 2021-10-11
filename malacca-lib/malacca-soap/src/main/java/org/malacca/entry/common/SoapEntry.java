package org.malacca.entry.common;

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
 * Author :chensheng 2020/4/3
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class SoapEntry extends AbstractAdvancedEntry {

    private static final String TYPE = "soapEntry";

    private String uri;

    private String wsdl;

    public SoapEntry(String id, String name, String uri) {
        super(id, name);
        this.uri = uri;
    }

    public SoapEntry() {
        super(TYPE, TYPE);
    }

    public SoapEntry(String id, String name) {
        super(id, name);
    }

    @Override
    public Message doHandleMessage(Message<?> message) {
        try {
            Message<?> resultMessage = getFlowExecutor().execute(getId(), message);
            return resultMessage;
        } catch (Exception e) {
            return MessageBuilder.error("soap服务处理失败", e).build();
        }
    }

    @Override
    public void setEntryKey() {
        super.entryKey = uri;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getWsdl() {
        return wsdl;
    }

    public void setWsdl(String wsdl) {
        this.wsdl = wsdl;
    }
}
