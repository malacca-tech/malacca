package org.malacca.component;

import com.alibaba.fastjson.JSONObject;
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
 * Author :chensheng 2020/8/14
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class ConsoleAdvancedComponent extends AbstractAdvancedComponent {

    public ConsoleAdvancedComponent(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        try {
            System.out.println(String.format("header: %s, \n payload: %s",
                    JSONObject.toJSONString(message.getContext()), message.getPayload()));
            return message;
        } catch (Exception e) {
            throw new MessagingException("console转换器处理失败", e);
        }
    }

    @Override
    public String getType() {
        return "console";
    }
}
