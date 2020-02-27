package org.malacca.component.output;

import org.malacca.component.AbstractComponent;
import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;

public class HttpOutComponent extends AbstractComponent {

    public static final String TYPE = "httpOut";

    public HttpOutComponent() {
        super(TYPE, TYPE);
    }

    public HttpOutComponent(String id, String name) {
        super(id, name);
    }

    private String url;

    private long timeout;

    @Override
    public Message handleMessage(Message<?> message) throws MessagingException {
        // TODO: 2020/2/25 to impl
        return getListener().onfinish(getId(), message);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
