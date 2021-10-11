package org.malacca.support.helper;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import freemarker.ext.dom.NodeModel;
import org.malacca.exception.FreeMarkerMessageHandlerException;
import org.malacca.messaging.Message;
import org.malacca.utils.CloseableUtils;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;

public class MessageFreeMarker extends FreeMarker {

    private Message message;

    public MessageFreeMarker() {
    }

    public MessageFreeMarker(Message message) {
        this.message = message;
        messageMap.put("message", message);
        messageMap.put("payload", message.getPayload());
        String payload = String.valueOf(message.getPayload());
        if (isJson(payload)) {
            try {
                JSONObject jsonObject = JSONObject.parseObject(payload);
                messageMap.put("json", jsonObject);
            } catch (Exception e) {
            }
        }
        if (isXml(payload)) {
            ByteArrayInputStream byteArrayInputStream = null;
            try {
                byteArrayInputStream = new ByteArrayInputStream(payload.getBytes("utf-8"));
                InputSource is = new InputSource(byteArrayInputStream);
                messageMap.put("xml", NodeModel.parse(is));
            } catch (Exception e) {
            } finally {
                CloseableUtils.close(byteArrayInputStream);
            }
        }
        setMap(message.getContext());
    }

    private boolean isJson(String content) {
        if (StrUtil.isNotBlank(content)) {
            return content.trim().startsWith("{") || content.trim().startsWith("[");
        }
        return false;
    }

    private boolean isXml(String content) {
        if (StrUtil.isNotBlank(content)) {
            return content.trim().startsWith("<");
        }
        return false;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
