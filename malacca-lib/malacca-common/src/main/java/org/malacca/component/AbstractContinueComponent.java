package org.malacca.component;

import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :yangxing 2021/5/14
 * </p>
 * <p>
 * Department :
 * </p>
 */
public abstract class AbstractContinueComponent extends AbstractAdvancedComponent {

    protected boolean useOriginMessage;

    public AbstractContinueComponent(String id, String name) {
        super(id, name);
    }

    @Override
    public Message handleMessage(Message<?> message) throws MessagingException {
        Message result = super.handleMessage(message);
        return useOriginMessage ? message : result;
    }

    public boolean isUseOriginMessage() {
        return useOriginMessage;
    }

    public void setUseOriginMessage(boolean useOriginMessage) {
        this.useOriginMessage = useOriginMessage;
    }

}

