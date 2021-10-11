package org.malacca.component.common;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.*;
import com.eviware.soapui.model.iface.Response;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.MessagingException;
import org.malacca.exception.SoapUiMessageHandlerException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.support.helper.MessageFreeMarker;


/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/8/6
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class SoapUiOutComponent extends AbstractAdvancedComponent {

    private String url;
    private String operationName;
    private String soapVersion;
    private String requestXml;

    private WsdlOperation operation;


    public SoapUiOutComponent(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {

        try {
            MessageFreeMarker freeMarker = new MessageFreeMarker(message);
            String xml = freeMarker.parseExpression(requestXml);
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                    , String.format("%s-FreeMarker配置:\n%s, \n解析requestXml: %s \n==>\n 结果:%s"
                            , logContext.getServiceId(), freeMarker.getMessageMap(), requestXml, xml)
                    , logContext);
            operation = getWsdlOperation();

            WsdlRequest request = operation.addNewRequest("soapui");

            request.setRequestContent(xml);

            WsdlSubmit submit = (WsdlSubmit) request.submit(new WsdlSubmitContext(request), false);

            Response response = submit.getResponse();

            String content = response.getContentAsString();

            Message resultMessage = MessageBuilder.withPayload(content).copyContext(message.getContext()).build();
            return resultMessage;
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new SoapUiMessageHandlerException("SoapUi组件执行失败", e);
        }
    }

    @Override
    public String getType() {
        return "soapui";
    }

    private WsdlOperation getWsdlOperation() throws Exception {
        if (operation == null) {
            operation = getWsdlInterface(url, operationName);
        }
        return operation;
    }

    private WsdlOperation getWsdlInterface(String url, String operationName) throws Exception {
        //根据wsdl地址获取接口
        WsdlInterface[] facesInfo = WsdlInterfaceFactory.importWsdl(new WsdlProject(url), url, false);

        if (facesInfo != null && facesInfo.length > 0) {
//			根据接口获取方法
            for (WsdlInterface wface : facesInfo) {
                //获取soap1.1的吧~
                if (soapVersion.equals(wface.getSoapVersion().getName())) {
                    WsdlOperation op = wface.getOperationByName(operationName);
                    return op;
                }
            }
        }
        throw new Exception("找不到operationName:" + operationName);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getSoapVersion() {
        return soapVersion;
    }

    public void setSoapVersion(String soapVersion) {
        this.soapVersion = soapVersion;
    }

    public String getRequestXml() {
        return requestXml;
    }

    public void setRequestXml(String requestXml) {
        this.requestXml = requestXml;
    }
}
