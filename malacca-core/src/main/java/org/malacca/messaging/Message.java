package org.malacca.messaging;

import java.util.Map;

public interface Message<T> {
    T getPayload();

    Map<String, Object> getContext();
}
