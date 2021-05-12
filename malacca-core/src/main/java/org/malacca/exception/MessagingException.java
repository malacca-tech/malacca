package org.malacca.exception;

import org.malacca.messaging.Message;

public class MessagingException extends RuntimeException {
    /**
     * 异常编码
     */
    private String code;

    /**
     * 异常描述
     */
    private String tips;

    /**
     * 异常
     */
    private Exception e;

    /**
     * 异常message
     */
    private Message<?> failedMessage;

    public MessagingException(String code, String tips, Exception e, Message<?> failedMessage) {
        super(tips, e);
        this.code = code;
        this.tips = tips;
        this.e = e;
        this.failedMessage = failedMessage;
    }

    public MessagingException(String code, String tips, Exception e) {
        super(tips, e);
        this.code = code;
        this.tips = tips;
        this.e = e;
    }

    public MessagingException(String tips, Exception e) {
        super(tips, e);
        this.tips = tips;
        this.e = e;
    }

    public MessagingException(Exception e) {
        super((String) null, e);
        this.e = e;
    }

    public MessagingException(String tips) {
        super(tips, (Exception) null);
        this.tips = tips;
    }

    public MessagingException(Exception e, Message<?> failedMessage) {
        super((String) null, e);
        this.e = e;
        this.failedMessage = failedMessage;
    }

    public MessagingException(String tips, Exception e, Message<?> failedMessage) {
        super(tips, e);
        this.tips = tips;
        this.e = e;
        this.failedMessage = failedMessage;
    }

    public MessagingException(String code, String tips) {
        super(tips, (Exception) null);
        this.code = code;
        this.tips = tips;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public Message<?> getFailedMessage() {
        return failedMessage;
    }

    public void setFailedMessage(Message<?> failedMessage) {
        this.failedMessage = failedMessage;
    }
}
