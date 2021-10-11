package org.malacca.entry.common;

import org.malacca.messaging.Message;
import org.springframework.util.StringUtils;
import org.springframework.ws.InvalidXmlException;
import org.springframework.ws.soap.SoapMessageCreationException;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.saaj.support.SaajUtils;
import org.springframework.ws.transport.TransportInputStream;
import org.xml.sax.SAXParseException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

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
public class MalaccaSaajSoapMessageFactory extends SaajSoapMessageFactory {

    @Override
    public void afterPropertiesSet() {
    }

    private MimeHeaders parseMimeHeaders(InputStream inputStream) throws IOException {
        MimeHeaders mimeHeaders = new MimeHeaders();
        if (inputStream instanceof TransportInputStream) {
            TransportInputStream transportInputStream = (TransportInputStream) inputStream;
            Iterator headerNames = transportInputStream.getHeaderNames();

            while (headerNames.hasNext()) {
                String headerName = (String) headerNames.next();
                Iterator headerValues = transportInputStream.getHeaders(headerName);

                while (headerValues.hasNext()) {
                    String headerValue = (String) headerValues.next();
                    StringTokenizer tokenizer = new StringTokenizer(headerValue, ",");

                    while (tokenizer.hasMoreTokens()) {
                        mimeHeaders.addHeader(headerName, tokenizer.nextToken().trim());
                    }
                }
            }
        }

        return mimeHeaders;
    }

    private InputStream checkForUtf8ByteOrderMark(InputStream inputStream) throws IOException {
        PushbackInputStream pushbackInputStream = new PushbackInputStream(new BufferedInputStream(inputStream), 3);
        byte[] bytes = new byte[3];

        int bytesRead;
        int n;
        for (bytesRead = 0; bytesRead < bytes.length; bytesRead += n) {
            n = pushbackInputStream.read(bytes, bytesRead, bytes.length - bytesRead);
            if (n <= 0) {
                break;
            }
        }

        if (bytesRead > 0 && !this.isByteOrderMark(bytes)) {
            pushbackInputStream.unread(bytes, 0, bytesRead);
        }

        return pushbackInputStream;
    }

    private boolean isByteOrderMark(byte[] bytes) {
        return bytes.length == 3 && bytes[0] == -17 && bytes[1] == -69 && bytes[2] == -65;
    }

    public SaajSoapMessage createWebServiceMessage() {
        try {
            MessageFactory messageFactory = MessageFactory.newInstance("SOAP 1.1 Protocol");
            SOAPMessage saajMessage = messageFactory.createMessage();
            this.postProcess(saajMessage);
            return new SaajSoapMessage(saajMessage, true, this.getMessageFactory());
        } catch (SOAPException var2) {
            throw new SoapMessageCreationException("Could not create empty message: " + var2.getMessage(), var2);
        }
    }

    public SaajSoapMessage createWebServiceMessage(Message<?> requestMessage) {
        try {
            Map<String, Object> context = requestMessage.getContext();
            MimeHeaders mimeHeaders = new MimeHeaders();
            String ws_version = String.valueOf(context.get("ws_version"));
            if (SoapVersion.SOAP_11.toString().equals(ws_version)) {
                mimeHeaders.setHeader("content-type", SoapVersion.SOAP_11.getContentType());
            } else {
                mimeHeaders.setHeader("content-type", SoapVersion.SOAP_12.getContentType());
            }
            mimeHeaders.setHeader("ws_uri", String.valueOf(context.get("ws_uri")));
            SOAPMessage saajMessage = this.getIntegrationMessageFactory(mimeHeaders).createMessage();
            this.postProcess(saajMessage);
            return new SaajSoapMessage(saajMessage, true, this.getMessageFactory());
        } catch (SOAPException var2) {
            throw new SoapMessageCreationException("Could not create empty message: " + var2.getMessage(), var2);
        }
    }


    public SaajSoapMessage createWebServiceMessage(InputStream inputStream) throws IOException {
        MimeHeaders mimeHeaders = this.parseMimeHeaders(inputStream);
        MessageFactory messageFactory = getIntegrationMessageFactory(mimeHeaders);

        try {
            inputStream = this.checkForUtf8ByteOrderMark(inputStream);
            SOAPMessage saajMessage = messageFactory.createMessage(mimeHeaders, inputStream);
            saajMessage.getSOAPPart().getEnvelope();
            this.postProcess(saajMessage);
            return new SaajSoapMessage(saajMessage, true, messageFactory);
        } catch (SOAPException var7) {
            String contentType = StringUtils.arrayToCommaDelimitedString(mimeHeaders.getHeader("Content-Type"));
            if (contentType.contains("startinfo")) {
                contentType = contentType.replace("startinfo", "start-info");
                mimeHeaders.setHeader("Content-Type", contentType);

                try {
                    SOAPMessage saajMessage = messageFactory.createMessage(mimeHeaders, inputStream);
                    this.postProcess(saajMessage);
                    return new SaajSoapMessage(saajMessage, true);
                } catch (SOAPException var6) {
                    ;
                }
            }

            SAXParseException parseException = this.getSAXParseException(var7);
            if (parseException != null) {
                throw new InvalidXmlException("Could not parse XML", parseException);
            } else {
                throw new SoapMessageCreationException("Could not create message from InputStream: " + var7.getMessage(), var7);
            }
        }
    }

    private SAXParseException getSAXParseException(Throwable ex) {
        if (ex instanceof SAXParseException) {
            return (SAXParseException) ex;
        } else {
            return ex.getCause() != null ? this.getSAXParseException(ex.getCause()) : null;
        }
    }

    private MessageFactory getIntegrationMessageFactory(MimeHeaders mimeHeaders) {
        String[] headers = mimeHeaders.getHeader("content-type");
        if (headers != null && headers.length > 0) {
            try {
                if (SaajUtils.getSaajVersion() >= 2) {
                    if (headers[0].contains("text/xml")) {
                        //soap 1.1
                        return MessageFactory.newInstance("SOAP 1.1 Protocol");
                    } else {
                        //soap 1.2
                        return MessageFactory.newInstance("SOAP 1.2 Protocol");
                    }
                } else if (SaajUtils.getSaajVersion() == 1) {
                    return MessageFactory.newInstance();
                } else {
                    if (SaajUtils.getSaajVersion() != 0) {
                        throw new IllegalStateException("SaajSoapMessageFactory requires SAAJ 1.1, which was not found on the classpath");
                    }

                    return MessageFactory.newInstance();
                }
            } catch (NoSuchMethodError var2) {
                throw new SoapMessageCreationException("Could not create SAAJ MessageFactory. Is the version of the SAAJ specification interfaces [" + SaajUtils.getSaajVersionString() + "] the same as the version supported by the application server?", var2);
            } catch (SOAPException var3) {
                throw new SoapMessageCreationException("Could not create SAAJ MessageFactory: " + var3.getMessage(), var3);
            }

        }
        throw new SoapMessageCreationException("Could not find Content-type in headers");
    }

}
