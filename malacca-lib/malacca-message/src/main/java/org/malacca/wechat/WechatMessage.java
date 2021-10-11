package org.malacca.wechat;

import com.alibaba.fastjson.JSONObject;

public abstract class WechatMessage {

    private String msgtype;
    private String agentid;
    private String touser;
    private String toparty;
    private String totag;

    public String toJSONString() {
        return JSONObject.toJSONString(this);
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
}
