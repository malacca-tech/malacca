package org.malacca.component.filter;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.FlowMessageHandlerException;
import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;
import org.malacca.support.helper.MessageFreeMarker;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2021/3/16
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class FlowFilter extends AbstractAdvancedComponent {

    private String config;

    public FlowFilter(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        try {
            MessageFreeMarker messageFreeMarker = new MessageFreeMarker(message);
            String result = messageFreeMarker.parseExpression(config);
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                    , String.format("%s-FreeMarker配置:\n%s, \n解析config: %s \n==>\n 结果:%s", logContext.getServiceId(), messageFreeMarker.getMessageMap(), config, result)
                    , logContext);
            Boolean isContinue = Boolean.valueOf(result);
            message.getMessageContext().setContinue(isContinue);
            return message;
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new FlowMessageHandlerException("流程处理组件执行失败", e);
        }
    }

    @Override
    public String getType() {
        return "flowFliter";
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
