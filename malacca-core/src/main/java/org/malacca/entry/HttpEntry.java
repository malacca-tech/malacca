package org.malacca.entry;

import org.malacca.messaging.Message;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/24
 * </p>
 * <p>
 * Department :
 * </p>
 */
// TODO: 2020/2/24 lib 里面实现
public class HttpEntry extends AbstractEntry {

    private String path;

    private String method;

    private static final String TYPE = "httpEntry";

    public HttpEntry() {
        super(TYPE,TYPE);
    }

    public HttpEntry(String id,String name){
        super(id,name);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Message handleMessage(Message<?> message) {
        // TODO: 2020/2/24 统一入口 组件消息 ，然后调用此方法 然后 获取下一个 组件 发出去
        return null;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
