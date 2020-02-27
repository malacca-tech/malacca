package org.malacca.flow;

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
     * 前面组件id
     */
    private String preComponentId;

    /**
     * 后面组件id
     */
    private String sufComponentId;

    /**
     * 通道类型
     */
    private ChannelType type;

    public FlowElement(String preComponentId, ChannelType type, String sufComponentId) {
        this.preComponentId = preComponentId;
        this.sufComponentId = sufComponentId;
        this.type = type;
    }

    public String getPreComponentId() {
        return preComponentId;
    }

    public void setPreComponentId(String preComponentId) {
        this.preComponentId = preComponentId;
    }

    public String getSufComponentId() {
        return sufComponentId;
    }

    public void setSufComponentId(String sufComponentId) {
        this.sufComponentId = sufComponentId;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }
}
