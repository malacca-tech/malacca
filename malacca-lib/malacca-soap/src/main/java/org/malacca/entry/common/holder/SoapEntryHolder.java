package org.malacca.entry.common.holder;

import com.alibaba.fastjson.JSONObject;
import org.malacca.entry.*;
import org.malacca.entry.common.*;
import org.malacca.entry.holder.AbstractEntryHolder;
import org.malacca.exception.MessagingException;
import org.malacca.exception.ServiceLoadException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.MessageEndpoint;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.transform.TransformerObjectSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

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
public class SoapEntryHolder extends AbstractEntryHolder<Entry> implements MessageEndpoint {

    private GlobalUriEndpointMapping uriEndpointMapping;

    private SoapMessageDispatcherServlet messageDispatcherServlet;

    private final TransformerSupportDelegate transformerSupportDelegate = new TransformerSupportDelegate();

    private Map<String, SoapEntry> soapEntryMap;

    @Override
    public void loadEntry(String path, Entry entry) throws ServiceLoadException {
        if (SoapEntry.class.isAssignableFrom(entry.getClass())) {
            uriEndpointMapping.setUsePath(true);
            uriEndpointMapping.registerEndpointViaFacade(path, this);
            loadWsdl(path, ((SoapEntry) entry).getWsdl());
            getSoapEntryMap().put(path, (SoapEntry) entry);
        } else {
            throw new ServiceLoadException("Soap入口不是SoapEntry.class 类型");
        }
    }

    @Override
    public void unloadEntry(String path, Entry entry) {
        uriEndpointMapping.unregisterEndpointViaFacade(path);
        getSoapEntryMap().remove(path);
        messageDispatcherServlet.unregisterDefinition(path);
    }

    private void loadWsdl(String path, String wsdl) {
        Resource resource = null;
        if (wsdl.toLowerCase().startsWith("http")) {
            try {
                resource = new SoapUrlResource(wsdl);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else if (wsdl.toLowerCase().startsWith("classpath:")) {
            resource = new ClassPathResource(wsdl.substring(wsdl.indexOf(":") + 1));
        }
        SimpleWsdl11Definition definition = new SimpleWsdl11Definition(resource);
        try {
            definition.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        messageDispatcherServlet.registerDefinition(path, definition);
    }

    @Override
    public void invoke(MessageContext messageContext) throws Exception {
        WebServiceMessage request = messageContext.getRequest();
        Assert.notNull(request, "Invalid message context: request was null.");
        Object payload = null;
        Source payloadSource = request.getPayloadSource();
        if (payloadSource instanceof DOMSource) {
//            JSONObject requestBody = new JSONObject();
//            DOMSource domSource = (DOMSource) payloadSource;
//            NodeList childNodes = domSource.getNode().getChildNodes();
//            if (childNodes != null) {
//                for (int i = 0; i < childNodes.getLength(); i++) {
//                    Node item = childNodes.item(i);
//                    requestBody.put(item.getNodeName(), item.getTextContent());
//                }
//            }
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(payloadSource, result);
            payload = stringWriter.getBuffer().toString();
//            payload = requestBody.toJSONString();
        } else {
            payload = payloadSource;
        }
        MessageBuilder builder = MessageBuilder.withPayload(payload);

        this.fromSoapHeaders(messageContext, builder);
        // TODO: 2020/4/3 获取path
        String uri = String.valueOf(((SoapMessageContext) messageContext).getRequestMessage().getContext().get("ws_uri"));

        SoapEntry entry = soapEntryMap.get(uri);
        if (entry == null) {
            // TODO: 2020/4/5 rizhi
            throw new MessagingException("ws未找到服务入口" + uri);
        }
        Message<?> replyMessage = entry.handleMessage(builder.build());

        if (replyMessage != null) {
            Object replyPayload = replyMessage.getPayload();
            Source responseSource = null;

            if (replyPayload instanceof WebServiceMessage) {
                messageContext.setResponse((WebServiceMessage) replyPayload);
            } else {
                if (replyPayload instanceof Source) {
                    responseSource = (Source) replyPayload;
                } else if (replyPayload instanceof Document) {
                    responseSource = new DOMSource((Document) replyPayload);
                } else if (replyPayload instanceof String) {
                    responseSource = new StringSource((String) replyPayload);
                } else {
                    throw new MessagingException(JSONObject.toJSONString(replyPayload));
                }
                WebServiceMessage response = messageContext.getResponse();
                this.transformerSupportDelegate.transformSourceToResult(responseSource, response.getPayloadResult());
            }
        }
    }

    private void fromSoapHeaders(MessageContext messageContext, MessageBuilder builder) {
        WebServiceMessage request = messageContext.getRequest();
        String[] propertyNames = messageContext.getPropertyNames();
        if (propertyNames != null) {
            for (String propertyName : propertyNames) {
                builder.setContext(propertyName, messageContext.getProperty(propertyName));
            }
        }
        if (request instanceof SoapMessage) {
            SoapMessage soapMessage = (SoapMessage) request;
            Map<String, ?> headers = SoapHolderMapper.extractStandardHeaders(soapMessage);
            if (!CollectionUtils.isEmpty(headers)) {
                builder.copyContext(headers);
            }
        }
    }

    public Map<String, SoapEntry> getSoapEntryMap() {
        if (soapEntryMap == null) {
            soapEntryMap = new HashMap<>(16);
        }
        return soapEntryMap;
    }

    public void setSoapEntryMap(Map<String, SoapEntry> soapEntryMap) {
        this.soapEntryMap = soapEntryMap;
    }

    private static class TransformerSupportDelegate extends TransformerObjectSupport {

        TransformerSupportDelegate() {
            super();
        }

        void transformSourceToResult(Source source, Result result) throws TransformerException {
            this.transform(source, result);
        }

    }

    public GlobalUriEndpointMapping getUriEndpointMapping() {
        return uriEndpointMapping;
    }

    public void setUriEndpointMapping(GlobalUriEndpointMapping uriEndpointMapping) {
        this.uriEndpointMapping = uriEndpointMapping;
    }

    public SoapMessageDispatcherServlet getMessageDispatcherServlet() {
        return messageDispatcherServlet;
    }

    public void setMessageDispatcherServlet(SoapMessageDispatcherServlet messageDispatcherServlet) {
        this.messageDispatcherServlet = messageDispatcherServlet;
    }
}
