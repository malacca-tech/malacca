package org.malacca.entry;

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
 * Author :chensheng 2020/3/2
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class CommonHttpEntry extends AbstractEntry {

    private String uri;

    private String method;

    private int port;

    /**
     * 消息体表达式
     */
    private String payloadExpression;

    private static final String TYPE = "httpEntry";

    public CommonHttpEntry() {
        super(TYPE, TYPE);
    }

    public CommonHttpEntry(String id, String name) {
        super(id, name);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Message handleMessage(Message<?> message) {
        // TODO: 2020/2/24 统一入口 组件消息 ，然后调用此方法 然后 获取下一个 组件 发出去
        Message<String> stringMessage = MessageBuilder.withPayload("1").build();
//        Message<?> resultMessage = getFlowExecutor().execute(getId(), stringMessage);
        return stringMessage;
    }

    @Override
    public void setEntryKey() {
        super.entryKey = uri;
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
