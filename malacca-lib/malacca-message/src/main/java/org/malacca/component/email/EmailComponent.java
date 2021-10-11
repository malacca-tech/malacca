package org.malacca.component.email;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.exception.EmailMessageHandlerException;
import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.support.helper.MessageFreeMarker;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :yangxing 2021/8/17
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class EmailComponent extends AbstractAdvancedComponent {

    private String host = "smtp.qq.com";

    private String port = "465";

    private String password;

    private String sender;

    private String from;

    private String toUser;

    private String ccUser;

    private String bccUser;

    private String subject;

    private String content;

    public EmailComponent(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        try {
            MessageFreeMarker freeMarker = new MessageFreeMarker(message);
            String hostStr = freeMarker.parseExpression(host);
            String portStr = freeMarker.parseExpression(port);
            String passwordStr = freeMarker.parseExpression(password);
            String senderStr = freeMarker.parseExpression(sender);
            String fromStr = freeMarker.parseExpression(from);
            String toUserStr = freeMarker.parseExpression(toUser);
            String subjectStr = freeMarker.parseExpression(subject);
            String contentStr = freeMarker.parseExpression(content);

            Properties props = OhMyEmail.defaultConfig(true);
            props.put("mail.smtp.host", hostStr);
            props.put("mail.smtp.port", portStr);
            OhMyEmail.config(props, senderStr, passwordStr);
            OhMyEmail ohMyEmail = OhMyEmail.subject(subjectStr)
                    .from(fromStr)
                    .to(toUserStr);
            if (StringUtils.hasText(ccUser)) {
                String ccUserStr = freeMarker.parseExpression(ccUser);
                ohMyEmail.cc(ccUserStr);
            }
            if (StringUtils.hasText(bccUser)) {
                String bccUserStr = freeMarker.parseExpression(bccUser);
                ohMyEmail.bcc(bccUserStr);
            }
            ohMyEmail.html(contentStr).send();
        } catch (EmailMessageHandlerException | javax.mail.MessagingException e) {
            throw new EmailMessageHandlerException("EMAIL发送错误", e);
        }
        Message resultMessage = MessageBuilder.success().copyContext(message.getContext()).setMessageContext(message.getMessageContext()).build();
        return resultMessage;
    }

    @Override
    public String getType() {
        return "email";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getCcUser() {
        return ccUser;
    }

    public void setCcUser(String ccUser) {
        this.ccUser = ccUser;
    }

    public String getBccUser() {
        return bccUser;
    }

    public void setBccUser(String bccUser) {
        this.bccUser = bccUser;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

