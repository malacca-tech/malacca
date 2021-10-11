package org.malacca.wechat;

import java.util.Date;

public class WechatToken {

    private String token;
    private Date date;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}