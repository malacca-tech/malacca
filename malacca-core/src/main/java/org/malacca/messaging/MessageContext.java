package org.malacca.messaging;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 整个消息流转过程中的上下文，进行流程控制，状态标识，辅助记录消息过程
 * </p>
 * <p>
 * Author :chensheng 2021/3/16
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class MessageContext {
    /**
     * 判断流程是否可以
     */
    private boolean isContinue = true;
    /**
     * 重推消息的父id
     */
    private String parentTraceId;
    /**
     * 类型（重推。。。）
     */
    private String type;

    public boolean isContinue() {
        return isContinue;
    }

    public void setContinue(boolean aContinue) {
        isContinue = aContinue;
    }

    public String getParentTraceId() {
        return parentTraceId;
    }

    public void setParentTraceId(String parentTraceId) {
        this.parentTraceId = parentTraceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
