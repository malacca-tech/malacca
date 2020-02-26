package org.malacca.utils;

import com.alibaba.fastjson.JSONObject;
import freemarker.ext.dom.NodeModel;
import org.malacca.messaging.Message;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: freemarker message 上下文
 * </p>
 * <p>
 * Author :chensheng 2020/2/26
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class MessageFreeMarkerUtils extends FreeMarkerUtils {

    private Message message;

    private boolean payloadToXml;

    private Map templateMap;

    public MessageFreeMarkerUtils() {
    }

    public MessageFreeMarkerUtils(Message message, boolean payloadToXml) {
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
                    // TODO: 2020/2/26
                    e.printStackTrace();
                }
            } catch (IOException e) {
                // TODO: 2020/2/26
                e.printStackTrace();
            } finally {
                CloseableUtils.close(byteArrayInputStream);
            }
        }
    }

    public MessageFreeMarkerUtils(Map templateMap) {
        this.templateMap = templateMap;
        messageMap.putAll(templateMap);
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
