package org.malacca.entity;


public abstract class Message {

    private String platform;

    private String msgType;

    private String agentId;

    private String toUser;

    private String toParty;

    private String toTag;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getToParty() {
        return toParty;
    }

    public void setToParty(String toParty) {
        this.toParty = toParty;
    }

    public String getToTag() {
        return toTag;
    }

    public void setToTag(String toTag) {
        this.toTag = toTag;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getMsgType(String msgtype) {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
