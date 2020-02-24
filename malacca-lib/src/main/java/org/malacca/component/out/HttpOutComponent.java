package org.malacca.component.out;

import org.malacca.component.AbstractComponent;
import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;

public class HttpOutComponent extends AbstractComponent {

    private String url;

    private String timeout;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    @Override
    public Message handleMessage(Message<?> message) throws MessagingException {
        return null;
    }
}
