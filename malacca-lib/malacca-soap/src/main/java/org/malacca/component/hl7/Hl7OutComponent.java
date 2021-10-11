package org.malacca.component.hl7;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.entry.hl7.Hl7Service;
import org.malacca.exception.Hl7MessageHandlerException;
import org.malacca.exception.MessagingException;
import org.malacca.log.LogType;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/6
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class Hl7OutComponent extends AbstractAdvancedComponent {

    private String action;

    private String wsdl;

    public Hl7OutComponent(String id, String name) {
        super(id, name);
    }

    @Override
    public Message doHandleMessage(Message<?> message) throws MessagingException {
        try {
            JaxWsProxyFactoryBean bean = new JaxWsProxyFactoryBean();
            bean.setServiceClass(Hl7Service.class);
            bean.setAddress(wsdl);
            Hl7Service hl7Service = (Hl7Service) bean.create();
            String result = hl7Service.HIPMessageServer(action, (String) message.getPayload());
            Message resultMessage = MessageBuilder.withPayload(result).copyContext(message.getContext()).build();
            return resultMessage;
        } catch (Exception e) {
            throw new Hl7MessageHandlerException("Hl7组件执行失败:wsdl:" + wsdl, e);
        }
    }

    @Override
    public String getType() {
        return "hl7";
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getWsdl() {
        return wsdl;
    }

    public void setWsdl(String wsdl) {
        this.wsdl = wsdl;
    }
}
