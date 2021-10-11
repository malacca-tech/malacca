package org.malacca.entry.common;

import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ws.FaultAwareWebServiceMessage;
import org.springframework.ws.InvalidXmlException;
import org.springframework.ws.NoEndpointFoundException;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.transport.FaultAwareWebServiceConnection;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.WebServiceMessageReceiver;
import org.springframework.ws.transport.context.DefaultTransportContext;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.WebServiceMessageReceiverHandlerAdapter;
import org.springframework.ws.transport.support.TransportUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class MessageReceiverHandlerAdapter extends WebServiceMessageReceiverHandlerAdapter {

    public ModelAndView handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        if ("POST".equals(httpServletRequest.getMethod())) {
            ServletConnection connection = new ServletConnection(httpServletRequest, httpServletResponse);
            try {
                this.handle(connection, (WebServiceMessageReceiver) handler, httpServletRequest.getRequestURI());
            } catch (InvalidXmlException var6) {
                this.handleInvalidXmlException(httpServletRequest, httpServletResponse, handler, var6);
            }
        } else {
            this.handleNonPostMethod(httpServletRequest, httpServletResponse, handler);
        }

        return null;
    }

    protected void handle(WebServiceConnection connection, WebServiceMessageReceiver receiver, String uri) throws Exception {
        this.logUri(connection);
        TransportContext previousTransportContext = TransportContextHolder.getTransportContext();
        TransportContextHolder.setTransportContext(new DefaultTransportContext(connection));

        try {
            WebServiceMessage request = connection.receive(this.getMessageFactory());
            Map<String, Object> headers = new HashMap<>();
            if (request instanceof SaajSoapMessage) {
                SoapVersion version = ((SaajSoapMessage) request).getVersion();
                if (version != null) {
                    headers.put("ws_version", version.toString());
                    headers.put("ws_uri", uri);
                }
            }
            Message<String> build = MessageBuilder.withPayload("").copyContext(headers).build();
            MessageContext messageContext = new SoapMessageContext(build, request, this.getMessageFactory());
            receiver.receive(messageContext);
            if (messageContext.hasResponse()) {
                WebServiceMessage response = messageContext.getResponse();
                if (response instanceof FaultAwareWebServiceMessage && connection instanceof FaultAwareWebServiceConnection) {
                    FaultAwareWebServiceMessage faultResponse = (FaultAwareWebServiceMessage) response;
                    FaultAwareWebServiceConnection faultConnection = (FaultAwareWebServiceConnection) connection;
                    faultConnection.setFaultCode(faultResponse.getFaultCode());
                }

                connection.send(messageContext.getResponse());
            }
        } catch (NoEndpointFoundException var12) {
            this.handleNoEndpointFoundException(var12, connection, receiver);
        } finally {
            TransportUtils.closeConnection(connection);
            TransportContextHolder.setTransportContext(previousTransportContext);
        }

    }

    private void logUri(WebServiceConnection connection) {
        if (this.logger.isDebugEnabled()) {
            try {
                this.logger.debug("Accepting incoming [" + connection + "] at [" + connection.getUri() + "]");
            } catch (URISyntaxException var3) {
                ;
            }
        }
    }
}
