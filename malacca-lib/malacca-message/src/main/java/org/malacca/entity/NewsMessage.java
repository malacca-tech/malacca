package org.malacca.entity;

public class NewsMessage extends Message {

    private News news = new News();

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
