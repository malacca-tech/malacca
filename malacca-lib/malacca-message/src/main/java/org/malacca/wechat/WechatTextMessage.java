package org.malacca.wechat;

public class WechatTextMessage extends WechatMessage {

    private WechatContext text = new WechatContext();

    public WechatTextMessage() {
        setMsgtype("text");
    }

    public WechatContext getText() {
        return text;
    }

    public void setText(WechatContext text) {
        this.text = text;
    }
}
