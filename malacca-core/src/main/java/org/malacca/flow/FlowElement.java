package org.malacca.flow;

import java.util.List;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/26
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class FlowElement {

    /**
     * 当前组件id
     */
    private String componentId;
    /**
     * 是否是同步
     */
    private boolean isSynchronized = true;
    /**
     * 是否是异常通道
     */
    private boolean isErrorChannel = false;

    /**
     * 路由使用的条件 ，支持freemarker语法
     */
    private String routerId;

    List<FlowElement> nextElements;

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean aSynchronized) {
        isSynchronized = aSynchronized;
    }

    public boolean isErrorChannel() {
        return isErrorChannel;
    }

    public void setErrorChannel(boolean errorChannel) {
        isErrorChannel = errorChannel;
    }

    public List<FlowElement> getNextElements() {
        return nextElements;
    }

    public void setNextElements(List<FlowElement> nextElements) {
        this.nextElements = nextElements;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }
}
