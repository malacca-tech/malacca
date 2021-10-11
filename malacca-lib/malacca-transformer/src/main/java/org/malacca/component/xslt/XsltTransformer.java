package org.malacca.component.xslt;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.exception.MessagingException;
import org.malacca.log.LogFactoryType;
import org.malacca.log.LogType;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.utils.CloseableUtils;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/5
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class XsltTransformer extends AbstractAdvancedComponent {

    public String config;

    public XsltTransformer(String id, String name) {
        super(id, name);
    }

    @Override
    public Message doHandleMessage(Message<?> message) throws MessagingException {
        String source = (String) message.getPayload();
        if (source != null) {
            ByteArrayOutputStream outputStream = null;
            ByteArrayInputStream byteArrayInputStream = null;
            ByteArrayInputStream byteArrayInput = null;
            try {
                TransformerFactory factory = TransformerFactory.newInstance();
                byteArrayInputStream = new ByteArrayInputStream(config.getBytes());
                Transformer transformer = factory.newTransformer(new StreamSource(byteArrayInputStream));
                outputStream = new ByteArrayOutputStream();
                byteArrayInput = new ByteArrayInputStream(source.getBytes());
                transformer.transform(new StreamSource(byteArrayInput), new StreamResult(outputStream));
                String result = outputStream.toString();
                Message resultMessage = MessageBuilder.withPayload(result).copyContext(message.getContext()).build();
                return resultMessage;
            } catch (Exception e) {
                throw new MessagingException("XSLT转换异常", e);
            } finally {
                CloseableUtils.close(outputStream);
                CloseableUtils.close(byteArrayInputStream);
                CloseableUtils.close(byteArrayInput);
            }
        } else {
            // TODO: 2020/10/27
            logger.info("xslt{}消息体为空", logContext, LogType.RESPONSE, message, getName());
        }
        return message;
    }

    @Override
    public String getType() {
        return "xslt";
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
