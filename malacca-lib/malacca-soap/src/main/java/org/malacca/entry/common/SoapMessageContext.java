package org.malacca.entry.common;

import org.malacca.messaging.Message;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/3
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class SoapMessageContext extends DefaultMessageContext {
    private Message<?> requestMessage;

    public SoapMessageContext(WebServiceMessageFactory messageFactory, Message<?> requestMessage) {
        this(requestMessage, ((MalaccaSaajSoapMessageFactory) messageFactory).createWebServiceMessage(requestMessage), messageFactory);
    }

    public SoapMessageContext(Message<?> requestMessage, WebServiceMessage request, WebServiceMessageFactory messageFactory) {
        super(request, messageFactory);
        this.requestMessage = requestMessage;
    }

    @Override
    public WebServiceMessage getResponse() {
        if (!hasResponse()) {
            SaajSoapMessage webServiceMessage = ((MalaccaSaajSoapMessageFactory) this.getMessageFactory()).createWebServiceMessage(this.requestMessage);
            super.setResponse(webServiceMessage);
        }
        return super.getResponse();
    }

    public Message<?> getRequestMessage() {
        return requestMessage;
    }
}
