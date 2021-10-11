package org.malacca.component;

import org.malacca.Config;
import org.malacca.MessageConfig;
import org.malacca.MessageMode;
import org.malacca.ProxyConfig;
import org.malacca.dingtalk.DingtalkConfig;
import org.malacca.dingtalk.DingtalkMessageKit;
import org.malacca.entity.MessageResult;
import org.malacca.entity.TextMessage;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.DingTalkMessageHandlerException;
import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;
import org.malacca.support.helper.MessageFreeMarker;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2021/3/24
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class DingTalkComponent extends AbstractAdvancedComponent {

    private String appKey;
    private String appSecret;
    private String mode;
    private String url;

    private String agentid;
    private String touser;
    private String toparty;
    private String totag;
    private String msgtype;

    private String payload;

//    private String dingTalkHost = "oapi.dingtalk.com";

    private MessageConfig dingtalkConfig;

    public DingTalkComponent(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        try {
            MessageFreeMarker messageFreeMarker = new MessageFreeMarker(message);
            TextMessage textMessage = getTextMessage(messageFreeMarker);
            MessageResult messageResult;
            if (mode.equals(MessageMode.Proxy.name())) {
                messageResult = DingtalkMessageKit.sendProxyMessage(url
                        , appKey
                        , appSecret
                        , agentid
                        , textMessage.getToUser()
                        , toparty
                        , totag
                        , msgtype
                        , textMessage.getText());
            } else {
                messageResult = DingtalkMessageKit.sendMessage(dingtalkConfig, textMessage);
            }
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                    , String.format("%s-请求message:\n%s, \n返回message:\n%s", logContext.getServiceId(), textMessage, messageResult)
                    , logContext);
            String code = messageResult.getCode();
            // TODO: 2021/4/6 判断code
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new DingTalkMessageHandlerException("钉钉消息组件执行异常", e);
        }
        return message;
    }

    private TextMessage getTextMessage(MessageFreeMarker messageFreeMarker) {
        TextMessage textMessage = new TextMessage();
        String tousers = messageFreeMarker.parseExpression(touser);
        String text = messageFreeMarker.parseExpression(payload);
        textMessage.setToUser(tousers);
        textMessage.setAgentId(agentid);
        textMessage.setToParty(toparty);
        textMessage.setPlatform("");
        textMessage.setToTag(totag);
        textMessage.setText(text);
        FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                , String.format("%s-FreeMarker配置:\n%s, \n解析touser: %s \n==>\n 结果:%s, \n解析payload: %s \n==>\n 结果:%s"
                        , logContext.getServiceId(), messageFreeMarker.getMessageMap(), touser, tousers, payload, text)
                , logContext);
        return textMessage;
    }

    public void initMessageConfig() {
        MessageConfig messageConfig = new MessageConfig();
        messageConfig.setMode(mode);
        Config config = new Config();
        DingtalkConfig dingtalkConfig = new DingtalkConfig();
        dingtalkConfig.setAppKey(appKey);
        dingtalkConfig.setAppSecret(appSecret);
//        dingtalkConfig.setDingTalkHost(dingTalkHost);
        config.setDingtalk(dingtalkConfig);
        ProxyConfig proxyConfig = new ProxyConfig();
        proxyConfig.setUrl(url);
        config.setProxy(proxyConfig);
        messageConfig.setConfig(config);
        this.dingtalkConfig = messageConfig;
    }

    @Override
    public String getType() {
        return "dingTalk";
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
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

//    public String getDingTalkHost() {
//        return dingTalkHost;
//    }
//
//    public void setDingTalkHost(String dingTalkHost) {
//        this.dingTalkHost = dingTalkHost;
//    }


    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public MessageConfig getDingtalkConfig() {
        return dingtalkConfig;
    }

    public void setDingtalkConfig(MessageConfig dingtalkConfig) {
        this.dingtalkConfig = dingtalkConfig;
    }
}
