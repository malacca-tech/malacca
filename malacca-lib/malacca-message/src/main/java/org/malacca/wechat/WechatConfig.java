package org.malacca.wechat;

import java.util.HashMap;
import java.util.Map;

public class WechatConfig {

    private String corpid;

    private Map<String, String> secret = new HashMap<>();

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public Map<String, String> getSecret() {
        return secret;
    }

    public void setSecret(Map<String, String> secret) {
        this.secret = secret;
    }
}
