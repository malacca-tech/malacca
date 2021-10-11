package org.malacca.entry;

import org.malacca.exception.MessagingException;
import org.malacca.exception.SpringHttpMessageHandlerException;
import org.malacca.messaging.Message;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/9/16
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class SpringHttpEntry extends AbstractAdvancedEntry {

    private String uri;

    private String method;

    private int port;

    /**
     * 消息体表达式
     */
    private String payloadExpression;

    private static final String TYPE = "springHttpEntry";

    public SpringHttpEntry(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        try {
            Message<?> resultMessage = getFlowExecutor().execute(getId(), message);
            return resultMessage;
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new SpringHttpMessageHandlerException("Http入口组件执行失败", e);
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPayloadExpression() {
        return payloadExpression;
    }

    public void setPayloadExpression(String payloadExpression) {
        this.payloadExpression = payloadExpression;
    }
}
