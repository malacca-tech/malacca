package org.malacca.component.js;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.exception.JSMessageHandlerException;
import org.malacca.exception.MessagingException;
import org.malacca.log.LogFactoryType;
import org.malacca.log.LogType;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/5
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class JavaScriptTransformer extends AbstractAdvancedComponent {

    private String config;

    public JavaScriptTransformer(String id, String name) {
        super(id, name);
    }

    @Override
    public Message doHandleMessage(Message<?> message) throws MessagingException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            engine.eval(config);
            if (engine instanceof Invocable) {
                Invocable in = (Invocable) engine;
                Object result = in.invokeFunction("handleJson", message.getPayload());
                Message resultMessage = MessageBuilder.withPayload(result).copyContext(message.getContext()).build();
                return resultMessage;
            }
        } catch (Exception e) {
            throw new JSMessageHandlerException("javaScript组件执行异常", e);
        }
        return message;
    }

    @Override
    public String getType() {
        return "javaScript";
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
