package org.malacca.messaging;

import java.util.Map;

public interface Message<T> {

    T getPayload();

    MessageContext getMessageContext();

    Map<String, Object> getContext();
}
