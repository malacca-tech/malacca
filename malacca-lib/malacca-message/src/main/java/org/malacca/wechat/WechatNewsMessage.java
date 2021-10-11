package org.malacca.wechat;

public class WechatNewsMessage extends WechatMessage {

    private WechatNews news = new WechatNews();

    public WechatNewsMessage() {
        setMsgtype("news");
    }

    public WechatNews getNews() {
        return news;
    }

    public void setNews(WechatNews news) {
        this.news = news;
    }
}
