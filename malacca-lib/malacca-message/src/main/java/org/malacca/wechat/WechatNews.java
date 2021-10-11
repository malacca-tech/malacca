package org.malacca.wechat;

import java.util.ArrayList;
import java.util.List;

public class WechatNews {

    private List<WechatArticle> articles=new ArrayList<>();

    public List<WechatArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<WechatArticle> articles) {
        this.articles = articles;
    }
}
