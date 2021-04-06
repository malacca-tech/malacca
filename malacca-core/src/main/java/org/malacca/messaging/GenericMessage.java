package org.malacca.messaging;


import cn.hutool.core.lang.Assert;

import java.io.Serializable;
import java.util.Map;

public class GenericMessage<T> implements Message<T>, Serializable {
    private static final long serialVersionUID = 4268801052358035098L;
    private final T payload;
    private final Map<String, Object> context;
    private final MessageContext messageContext;

    public GenericMessage(T payload) {
        this(payload, null);
    }

    public GenericMessage(T payload, Map<String, Object> context) {
        this(payload, context, new MessageContext());
    }

    public GenericMessage(T payload, Map<String, Object> context, MessageContext messageContext) {
        Assert.notNull(payload, "Payload must not be null");
        Assert.notNull(context, "MessageHeaders must not be null");
        this.payload = payload;
        this.context = context;
        this.messageContext = messageContext;
    }

    public T getPayload() {
        return this.payload;
    }

    @Override
    public MessageContext getMessageContext() {
        return this.messageContext;
    }

    @Override
    public Map<String, Object> getContext() {
        return this.context;
    }


    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof GenericMessage)) {
            return false;
        } else {
            GenericMessage<?> otherMsg = (GenericMessage) other;
            return this.payload.equals(otherMsg.payload) && this.context.equals(otherMsg.context);
        }
    }

    public int hashCode() {
        if (payload != null) {
            return this.payload.hashCode() * 23 + this.context.hashCode();
        } else {
            return this.context.hashCode();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [payload=");
        if (this.payload instanceof byte[]) {
            sb.append("byte[").append(((byte[]) ((byte[]) this.payload)).length).append("]");
        } else {
            sb.append(this.payload);
        }

        sb.append(", headers=").append(this.context).append("]");
        return sb.toString();
    }
}
