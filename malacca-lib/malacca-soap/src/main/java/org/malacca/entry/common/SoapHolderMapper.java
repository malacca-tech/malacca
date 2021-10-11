package org.malacca.entry.common;

import org.malacca.constant.SoapHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
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
public class SoapHolderMapper {

    private static final Logger LOG = LoggerFactory.getLogger(SoapHolderMapper.class);

    public static Map<String, Object> extractStandardHeaders(SoapMessage source) {
        Map<String, Object> headers = new HashMap<String, Object>(8);
        SoapVersion version = source.getVersion();
        SaajSoapMessage message = (SaajSoapMessage) source;
        SOAPMessage saajMessage = message.getSaajMessage();
        try {
            SOAPHeader soapHeader = saajMessage.getSOAPHeader();
            if (soapHeader != null) {
                NodeList childNodes = soapHeader.getChildNodes();
                if (childNodes != null) {
                    for (int i = 0; i < childNodes.getLength(); i++) {
                        Node item = childNodes.item(i);
                        headers.put(item.getNodeName(), item.getTextContent());
                    }
                }
            }
        } catch (SOAPException e) {
            LOG.error("获取webservice的头部消息失败", e);
        }
        if (version != null) {
            headers.put("ws_version", version.toString());
        }
        final String soapAction = source.getSoapAction();
        if (StringUtils.hasText(soapAction)) {
            headers.put(SoapHeader.SOAP_ACTION, soapAction);
        }
        return headers;
    }
}
