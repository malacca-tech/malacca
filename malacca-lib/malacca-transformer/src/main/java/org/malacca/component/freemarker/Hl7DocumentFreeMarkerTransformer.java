package org.malacca.component.freemarker;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.FreeMarkerMessageHandlerException;
import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.support.helper.MessageFreeMarker;
import org.malacca.support.util.Base64Kit;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/8/5
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class Hl7DocumentFreeMarkerTransformer extends AbstractAdvancedComponent {

    enum TYPE {
        JSON("json"), XML("xml");
        private String name;

        TYPE(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * cda文档 配置
     */
    private String cdaConfig;

    /**
     * 完整消息配置
     */
    private String serviceConfig;

    /**
     * 消息类型
     */
    private String type;

    public Hl7DocumentFreeMarkerTransformer(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        try {
            MessageFreeMarker messageFreeMarker = new MessageFreeMarker(message);
            String cda = messageFreeMarker.parseExpression(cdaConfig);
            messageFreeMarker.setVariable("cda", Base64Kit.encode(cda));
            String payload = messageFreeMarker.parseExpression(serviceConfig);
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                    , String.format("%s-FreeMarker配置:\n%s, \n解析cdaConfig: %s \n==>\n 结果:%s, \n解析serviceConfig: %s \n==>\n 结果:%s"
                            , logContext.getServiceId(), messageFreeMarker.getMessageMap(), cdaConfig, cda, serviceConfig, payload)
                    , logContext);
            Message resultMessage = MessageBuilder.withPayload(payload).copyContext(message.getContext()).build();
            return resultMessage;
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new FreeMarkerMessageHandlerException("hl7freemarker组件执行失败", e);
        }
    }


    @Override
    public String getType() {
        return "hl7Freemarker";
    }

    public String getCdaConfig() {
        return cdaConfig;
    }

    public void setCdaConfig(String cdaConfig) {
        this.cdaConfig = cdaConfig;
    }

    public String getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(String serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public void setType(String type) {
        this.type = type;
    }
}
