package org.malacca.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.internal.util.StringUtils;
import org.malacca.MessageConfig;
import org.malacca.MessageMode;
import org.malacca.entity.*;
import org.malacca.kit.HttpKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WechatMessageKit {

    public static MessageResult sendMessage(MessageConfig messageConfig, Message message) throws Exception {
        MessageResult messageResult = new MessageResult();
        String string = null;
        if (messageConfig.getMode().equals(MessageMode.Proxy.name().toLowerCase())) {
            String url = getUrl(messageConfig);
            Map<String, String> params = new HashMap<>();
            params.put("message", JSONObject.toJSONString(getWechatMessage(message)));
            string = HttpKit.postForm(url, params);
        } else {
            WechatConfig wechatConfig = messageConfig.getConfig().getWechat();
            String secret = wechatConfig.getSecret().get(message.getAgentId());
            String tokenValue = WechatTokenKit.getToken(wechatConfig.getCorpid(), message.getAgentId(), secret);
            String messageUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send";
            String url = messageUrl + "?access_token=" + tokenValue;
            string = HttpKit.postBody(url, null, "application/json", JSONObject.toJSONString(getWechatMessage(message)));
        }
        JSONObject result = JSON.parseObject(string);
        WechatResult wechatResult = JSONObject.toJavaObject(result, WechatResult.class);
        messageResult.setCode(wechatResult.getErrcode() == 0 ? MessageResult.CodeType.Success.name() : MessageResult.CodeType.Failure.name());
        messageResult.setMessage(wechatResult.getErrmsg());
        return messageResult;
    }

    public static WechatMessage getWechatMessage(Message message) {
        if (message instanceof TextMessage) {
            WechatTextMessage textMessage = new WechatTextMessage();
            WechatContext context = new WechatContext();
            context.setContent(((TextMessage) message).getText());
            textMessage.setAgentid(message.getAgentId());
            textMessage.setToparty(message.getToParty());
            textMessage.setTotag(message.getToTag());
            textMessage.setTouser(message.getToUser());
            textMessage.setText(context);
            return textMessage;
        } else if (message instanceof NewsMessage) {
            NewsMessage newsMessage = (NewsMessage) message;

            WechatNewsMessage wechatNewsMessage = new WechatNewsMessage();
            ArrayList<WechatArticle> articleArrayList = new ArrayList<>();

            WechatArticle wechatArticle = new WechatArticle();
            Article article = newsMessage.getNews().getArticle();
            wechatArticle.setTitle(article.getTitle());
            wechatArticle.setDescription(article.getDescription());
            wechatArticle.setUrl(article.getUrl());
            wechatArticle.setPicurl(article.getPicUrl());
            articleArrayList.add(wechatArticle);

            WechatNews wechatNews = new WechatNews();
            wechatNews.setArticles(articleArrayList);

            wechatNewsMessage.setAgentid(message.getAgentId());
            wechatNewsMessage.setToparty(message.getToParty());
            wechatNewsMessage.setTotag(message.getToTag());
            wechatNewsMessage.setTouser(message.getToUser());
            wechatNewsMessage.setNews(wechatNews);
            return wechatNewsMessage;
        } else {
            throw new RuntimeException("暂不支持除text、news以外的消息类型");
        }
    }

    private static String getUrl(MessageConfig messageConfig) {
        String url = messageConfig.getConfig().getProxy().getUrl();
        return url + "/wechat/sendMessage";
    }

    public static String post(String corpid, String secret, String api, String param) throws Exception {
        String tokenValue = WechatTokenKit.getToken(corpid, secret);
        String url = api + "?access_token=" + tokenValue;
        String response = HttpKit.postBody(url, null, "application/json", param);
        return response;
    }

    public static String get(String corpid, String secret, String api, String param) throws Exception {
        String tokenValue = WechatTokenKit.getToken(corpid, secret);
        String url = api + "?access_token=" + tokenValue;
        if (StringUtils.isEmpty(param) == false) {
            url += "&" + param;
        }
        String response = HttpKit.get(url);
        return response;
    }

}
