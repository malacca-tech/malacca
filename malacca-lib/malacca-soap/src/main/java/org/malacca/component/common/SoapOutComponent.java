package org.malacca.component.common;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;
import groovy.xml.MarkupBuilder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.WsdlResultParser;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.MessagingException;
import org.malacca.exception.SoapMessageHandlerException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.support.helper.MessageFreeMarker;
import org.springframework.ws.soap.SoapVersion;

import java.io.StringWriter;
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
 * Author :chensheng 2020/3/13
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class SoapOutComponent extends AbstractAdvancedComponent {

    public static final String ADVANCED_MODE_KEY = "ADVANCED";

    public static final String SIMPLE_MODE_KEY = "SIMPLE";

    private String wsdl;

    private String nameSpace;

    private String wsdlService;

    private String portTypeName;

    private String bindingName = "webserviceSoapBinding";

    private String operationName;

    private String requestXml;

    private String resultNodeName;

    private String mode = ADVANCED_MODE_KEY;

    private Map<String, String> params;

    private boolean needAction = false;

    private String soapAction = "";

    private int timeout = 6000;

    private String soapVersion;

    public SoapOutComponent(String id, String name) {
        super(id, name);
    }

    public SoapOutComponent() {
        super("soap", "soap");
    }

    @Override
    public Message doHandleMessage(Message<?> message) throws MessagingException {
        try {
            MessageFreeMarker freeMarker = new MessageFreeMarker(message);
            String xml = freeMarker.parseExpression(requestXml);
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                    , String.format("%s-FreeMarker配置:\n%s, \n解析requestXml: %s \n==>\n 结果:%s"
                            , logContext.getServiceId(), freeMarker.getMessageMap(), requestXml, xml)
                    , logContext);
            HashMap<String, String> headers = new HashMap<>();
            if (needAction) {
                headers.put("SOAPAction", soapAction);
            }
            Map<String, Object> context = message.getContext();
            String contentType = "";
//            String ws_version = String.valueOf(context.get("ws_version"));
            if (SoapVersion.SOAP_11.toString().equals(soapVersion)) {
                contentType = SoapVersion.SOAP_11.getContentType();
            } else {
                contentType = SoapVersion.SOAP_12.getContentType();
            }

            HttpRequest request = HttpUtil.createPost(wsdl).headerMap(headers, true).contentType(contentType + ";charset=UTF-8").body(xml).timeout(this.timeout);
            String soap = request.execute().body();
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                    , String.format("%s-请求\n%s, \n返回:\n%s", logContext.getServiceId(), wsdl, soap), logContext);

            String result = WsdlResultParser.parse(resultNodeName, soap);
            Message resultMessage = MessageBuilder.withPayload(result).copyContext(message.getContext()).build();
            return resultMessage;
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new SoapMessageHandlerException("Soap组件执行失败", e);
        }
    }

    public void init() throws DocumentException {
        if (SIMPLE_MODE_KEY.equals(mode)) {
            WSDLParser parser = new WSDLParser();
            Definitions definitions = parser.parse(wsdl);
            StringWriter writer = new StringWriter();
            SOARequestCreator creator = new SOARequestCreator(definitions, new RequestTemplateCreator(), new MarkupBuilder(writer));
            creator.createRequest(portTypeName, operationName, bindingName);
            String xml = writer.toString();
            Document doc;
            try {
                doc = DocumentHelper.parseText(xml);
            } catch (DocumentException e) {
                // TODO: 2020/3/13
                e.printStackTrace();
                throw e;
            }
            Element root = doc.getRootElement();
            Element elementBody = root.element("Body");
            Element element = (Element) elementBody.elements().get(0);
            if (params != null) {
                for (Object o : element.elements()) {
                    Element e = (Element) o;
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        if (e.getName().equals(entry.getKey())) {
                            e.setText(entry.getValue() != null ? entry.getValue() : "");
                        }
                    }
                }
            }
            requestXml = doc.asXML();
        }
    }

    public String getWsdl() {
        return wsdl;
    }

    public void setWsdl(String wsdl) {
        this.wsdl = wsdl;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getWsdlService() {
        return wsdlService;
    }

    public void setWsdlService(String wsdlService) {
        this.wsdlService = wsdlService;
    }

    public String getPortTypeName() {
        return portTypeName;
    }

    public void setPortTypeName(String portTypeName) {
        this.portTypeName = portTypeName;
    }

    public String getBindingName() {
        return bindingName;
    }

    public void setBindingName(String bindingName) {
        this.bindingName = bindingName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getRequestXml() {
        return requestXml;
    }

    public void setRequestXml(String requestXml) {
        this.requestXml = requestXml;
    }

    public String getResultNodeName() {
        return resultNodeName;
    }

    public void setResultNodeName(String resultNodeName) {
        this.resultNodeName = resultNodeName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public boolean isNeedAction() {
        return needAction;
    }

    public void setNeedAction(boolean needAction) {
        this.needAction = needAction;
    }

    public String getSoapAction() {
        return soapAction;
    }

    public void setSoapAction(String soapAction) {
        this.soapAction = soapAction;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getSoapVersion() {
        return soapVersion;
    }

    public void setSoapVersion(String soapVersion) {
        this.soapVersion = soapVersion;
    }

    @Override
    public String getType() {
        return "soap";
    }
}
