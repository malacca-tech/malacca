package org.malacca.dingtalk;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import org.malacca.MessageConfig;
import org.malacca.MessageMode;
import org.malacca.entity.*;
import org.malacca.kit.HttpKit;

import java.util.HashMap;
import java.util.Map;


public class DingtalkMessageKit {

    public static MessageResult sendMessage(MessageConfig messageConfig, Message message) throws Exception {
        DingtalkConfig dingtalkConfig = messageConfig.getConfig().getDingtalk();
        String tokenValue = DingtalkTokenKit.getDingtalkToken(dingtalkConfig.getAppKey(), dingtalkConfig.getAppSecret(), dingtalkConfig.getDingTalkHost());
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        DingtalkMessage dingtalkMessage = getDingtalkMessage(message);
        OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
        req.setAgentId(Long.valueOf(dingtalkMessage.getAgentid()));
        req.setUseridList(dingtalkMessage.getTouser());
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        if (dingtalkMessage instanceof DingtalkTextMessage) {
            DingtalkTextMessage dingtalkTextMessage = (DingtalkTextMessage) dingtalkMessage;
            msg.setMsgtype("text");
            OapiMessageCorpconversationAsyncsendV2Request.Text text = new OapiMessageCorpconversationAsyncsendV2Request.Text();
            text.setContent(dingtalkTextMessage.getText().getContent());
            msg.setText(text);
        } else {
            DingtalkCardMessage dingtalkCardMessage = (DingtalkCardMessage) dingtalkMessage;
            msg.setMsgtype("action_card");
            OapiMessageCorpconversationAsyncsendV2Request.ActionCard card = new OapiMessageCorpconversationAsyncsendV2Request.ActionCard();
            DingtalkCard action_card = dingtalkCardMessage.getAction_card();
            card.setTitle(action_card.getTitle());
            card.setMarkdown(action_card.getMarkdown());
            card.setSingleTitle(action_card.getSingle_title());
            card.setSingleUrl(action_card.getSingle_url());
            msg.setActionCard(card);
        }
        req.setMsg(msg);
        OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, tokenValue);
        MessageResult messageResult = new MessageResult();
        messageResult.setCode(rsp.getErrcode() == 0 ? MessageResult.CodeType.Success.name() : MessageResult.CodeType.Failure.name());
        messageResult.setMessage(rsp.getErrmsg());
        return messageResult;
    }

    public static MessageResult sendProxyMessage(String url, String appKey, String appSecret, String agentid, String touser, String toparty, String totag, String msgtype, String message) throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("appKey", appKey);
        params.put("appSecret", appSecret);
        params.put("agentid", agentid);
        params.put("touser", touser);
        params.put("toparty", toparty);
        params.put("totag", totag);
        params.put("msgtype", msgtype);
        String string = HttpKit.service(url, null, "POST", params, null, message);
        MessageResult messageResult = JSONObject.parseObject(string, MessageResult.class);
        return messageResult;
    }

    public static DingtalkMessage getDingtalkMessage(Message message) {
        if (message instanceof TextMessage) {
            DingtalkTextMessage textMessage = new DingtalkTextMessage();
            DingtalkContent context = new DingtalkContent();

            context.setContent(((TextMessage) message).getText());
            textMessage.setAgentid(message.getAgentId());
            textMessage.setToparty(message.getToParty());
            textMessage.setTotag(message.getToTag());
            textMessage.setTouser(message.getToUser());
            textMessage.setText(context);
            return textMessage;
        } else if (message instanceof NewsMessage) {
            NewsMessage newsMessage = (NewsMessage) message;

            DingtalkCardMessage dingtalkCardMessage = new DingtalkCardMessage();
            DingtalkCard action_card = dingtalkCardMessage.getAction_card();

            Article article = newsMessage.getNews().getArticle();
            action_card.setTitle(article.getTitle());
            action_card.setMarkdown(article.getDescription());
            action_card.setSingle_url(article.getUrl());
            action_card.setSingle_title("查看详情");

            dingtalkCardMessage.setAgentid(message.getAgentId());
            dingtalkCardMessage.setToparty(message.getToParty());
            dingtalkCardMessage.setTotag(message.getToTag());
            dingtalkCardMessage.setTouser(message.getToUser());
            dingtalkCardMessage.setAction_card(action_card);

            return dingtalkCardMessage;
        } else {
            throw new RuntimeException("暂不支持除text、news以外的消息类型");
        }
    }
}
