package org.malacca;


import org.malacca.dingtalk.DingtalkConfig;
import org.malacca.wechat.WechatConfig;

public class Config {
    private DingtalkConfig dingtalk;

    private WechatConfig wechat;

    private ProxyConfig proxy;

    public DingtalkConfig getDingtalk() {
        return dingtalk;
    }

    public void setDingtalk(DingtalkConfig dingtalk) {
        this.dingtalk = dingtalk;
    }

    public WechatConfig getWechat() {
        return wechat;
    }

    public void setWechat(WechatConfig wechat) {
        this.wechat = wechat;
    }

    public ProxyConfig getProxy() {
        return proxy;
    }

    public void setProxy(ProxyConfig proxy) {
        this.proxy = proxy;
    }
}
