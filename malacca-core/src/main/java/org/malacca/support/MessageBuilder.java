package org.malacca.support;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import org.malacca.messaging.GenericMessage;
import org.malacca.messaging.Message;
import org.malacca.messaging.MessageContext;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/23
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class MessageBuilder<T> {

    private Map<String, Object> context = new HashMap<>();

    private T payload;

    private final Message<T> originalMessage;

    private MessageContext messageContext = new MessageContext();

    private MessageBuilder(T payload, Message<T> originalMessage) {
        Assert.notNull(payload, "payload must not be null");
        this.payload = payload;
        this.originalMessage = originalMessage;
    }

    public static <T> MessageBuilder<T> withPayload(T payload) {
        return new MessageBuilder<>(payload, null);
    }

    public MessageBuilder copyContext(Map<String, Object> context) {
        if (context != null) {
            this.context.putAll(context);
        }
        return this;
    }

    public MessageBuilder setMessageContext(MessageContext messageContext) {
        if (messageContext != null) {
            this.messageContext = messageContext;
        }
        return this;
    }

    public static <T> MessageBuilder<T> fromMessage(Message<T> message) {
        Assert.notNull(message, "message must not be null");
        return new MessageBuilder(message.getPayload(), message);
    }

    public MessageBuilder setContext(String key, Object value) {
        this.context.put(key, value);
        return this;
    }

    public MessageBuilder removeContext(String key) {
        this.context.remove(key);
        return this;
    }

    public MessageBuilder removeContext(String key, Object value) {
        this.context.remove(key, value);
        return this;
    }

    public static MessageBuilder success() {
        // TODO: 2020/2/27 默认的返回成功的格式
        JSONObject successJson = new JSONObject();
        successJson.put("code", "1");
        MessageBuilder<JSONObject> builder = new MessageBuilder<>(successJson, null);
        return builder;
    }

    public static MessageBuilder error(String tips, Exception e) {
        // TODO: 2020/2/27 默认的返回失败的格式
        JSONObject successJson = new JSONObject();
        successJson.put("code", "0");
        successJson.put("tips", tips);
        successJson.put("cause", e.getMessage());
        MessageBuilder<JSONObject> builder = new MessageBuilder<>(successJson, null);
        return builder;
    }

    public Message<T> build() {
        return (Message) new GenericMessage(this.payload, context, messageContext);
    }
}
