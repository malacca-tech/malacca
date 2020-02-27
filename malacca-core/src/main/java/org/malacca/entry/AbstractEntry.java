package org.malacca.entry;

import org.malacca.messaging.Message;
import org.malacca.service.Listener;
import org.malacca.support.MessageBuilder;

import java.util.Map;

/**
 * 此类为入口的抽象类，供http,soap,数据库输入继承
 */
public abstract class AbstractEntry implements Entry {
    private String id;
    private String name;
    /**
     * 判断此组件是否启用
     */
    private boolean status;

    /**
     * 组件内部使用的环境变量
     */
    private Map<String, String> env;

    protected String entryKey;

    protected Listener listener;

    protected AbstractEntry(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Message buildMessage(Map<String, Object> headers, Object payload) {
        return MessageBuilder.withPayload(payload).copyContext(headers).build();
    }

    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public void setEnv(Map<String, String> env) {
        this.env = env;
    }

    @Override
    public String getEntryKey() {
        return entryKey;
    }

    public Listener getListener() {
        return listener;
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}