package org.malacca.utils;

import com.alibaba.fastjson.JSONObject;
import freemarker.ext.dom.NodeModel;
import org.malacca.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public class MessageFreeMarker extends FreeMarker {

    private Message message;

    private boolean payloadToXml;

    public MessageFreeMarker() {
    }

    public MessageFreeMarker(Message message, boolean payloadToXml) throws Exception {
        this.message = message;
        this.payloadToXml = payloadToXml;
        messageMap.put("message", message);
        messageMap.put("payload", message.getPayload());
        try {
            JSONObject jsonObject = JSONObject.parseObject(String.valueOf(message.getPayload()));
            messageMap.put("json", jsonObject);
        } catch (Exception e) {

        }
        setMap(message.getContext());
        if (payloadToXml && message.getPayload() != null) {
            ByteArrayInputStream byteArrayInputStream = null;
            try {
                byteArrayInputStream = new ByteArrayInputStream(String.valueOf(message.getPayload()).getBytes("utf-8"));
                InputSource is = new InputSource(byteArrayInputStream);
                try {
                    messageMap.put("xml", NodeModel.parse(is));
                } catch (Exception e) {
                    //LOG.error("DOM转换异常", e);
                }
            } catch (IOException e) {
                throw new Exception("输入输出异常", e);
            } finally {
                CloseableUtils.close(byteArrayInputStream);
            }
        }
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isPayloadToXml() {
        return payloadToXml;
    }

    public void setPayloadToXml(boolean payloadToXml) {
        this.payloadToXml = payloadToXml;
    }
}
