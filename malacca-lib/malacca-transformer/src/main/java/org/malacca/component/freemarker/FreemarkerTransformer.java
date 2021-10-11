package org.malacca.component.freemarker;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.FreeMarkerMessageHandlerException;
import org.malacca.exception.MessagingException;
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
 * Author :chensheng 2020/4/15
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class FreemarkerTransformer extends AbstractAdvancedComponent {

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

    private String config;

    private String type;

    public FreemarkerTransformer(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        try {
            MessageFreeMarker messageFreeMarker = new MessageFreeMarker(message);
            String payload = messageFreeMarker.parseExpression(config);
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                    , String.format("%s-FreeMarker配置:\n%s, \n解析config: %s \n==>\n 结果:%s"
                            , logContext.getServiceId(), messageFreeMarker.getMessageMap(), config, payload)
                    , logContext);
            Message resultMessage = MessageBuilder.withPayload(payload).copyContext(message.getContext()).build();
            return resultMessage;
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new FreeMarkerMessageHandlerException("freemarker组件执行失败", e);
        }
    }

    @Override
    public String getType() {
        return "freemarker";
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public void setType(String type) {
        this.type = type;
    }
}
