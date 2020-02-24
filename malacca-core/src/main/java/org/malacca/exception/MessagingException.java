package org.malacca.exception;

import org.malacca.messaging.Message;

public class MessagingException extends RuntimeException {
    private final Message<?> failedMessage;

    public MessagingException(Message<?> message) {
        super((String) null, (Throwable) null);
        this.failedMessage = message;
    }

    public MessagingException(String description) {
        super(description);
        this.failedMessage = null;
    }

    public MessagingException( String description, Throwable cause) {
        super(description, cause);
        this.failedMessage = null;
    }

    public MessagingException(Message<?> message, String description) {
        super(description);
        this.failedMessage = message;
    }

    public MessagingException(Message<?> message, Throwable cause) {
        super((String) null, cause);
        this.failedMessage = message;
    }

    public MessagingException(Message<?> message,  String description,  Throwable cause) {
        super(description, cause);
        this.failedMessage = message;
    }

    public Message<?> getFailedMessage() {
        return this.failedMessage;
    }

    public String toString() {
        return super.toString() + (this.failedMessage == null ? "" : ", failedMessage=" + this.failedMessage);
    }
}
