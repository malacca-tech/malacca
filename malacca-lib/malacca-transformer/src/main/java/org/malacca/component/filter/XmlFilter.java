package org.malacca.component.filter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.MessagingException;
import org.malacca.exception.XmlMessageHandlerException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.support.helper.FreeMarker;

import java.util.*;

/**
 * XML格式消息过滤
 */
public class XmlFilter extends AbstractAdvancedComponent {

    private String dataPath;
    private String xpath;
    private Map<String, String> conditionMap;

    public XmlFilter(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        try {
            String payload = message.getPayload().toString();
            String result = handleFreemarker(payload);
            Message resultMessage = MessageBuilder.withPayload(result).copyContext(message.getContext()).build();
            boolean isContinue = this.isContinue(result);
            resultMessage.getMessageContext().setContinue(isContinue);
            return resultMessage;
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new XmlMessageHandlerException("XML组件执行失败", e);
        }
    }

    public boolean isContinue(String resultPayload) {
        try {
            Document document = DocumentHelper.parseText(resultPayload);
            List<Element> elements = document.selectNodes(xpath);
            return !elements.isEmpty();
        } catch (DocumentException e) {
            throw new XmlMessageHandlerException("XML解析异常", e);
        }
    }

    public String handleFreemarker(String payload) {
        try {
            Document document = DocumentHelper.parseText(payload);
            List<Element> elements = document.selectNodes(xpath);
            FreeMarker freeMarker = new FreeMarker();
            for (Element element : elements) {
                boolean isFilter = true;
                for (Map.Entry<String, String> entry : conditionMap.entrySet()) {
                    String key = entry.getKey();
                    String condition = entry.getValue();
                    String value = element.elementText(key);
                    freeMarker.setVariable(key, value);
                    String config = "<#if " + key + condition + ">true</#if>";
                    String result = freeMarker.parseExpression(config);
                    FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                            , String.format("%s-FreeMarker配置:\n%s, \n解析config: %s \n==>\n 结果:%s"
                                    , logContext.getServiceId(), freeMarker.getMessageMap(), config, result)
                            , logContext);
                    if (!Boolean.parseBoolean(result)) {
                        isFilter = false;
                        break;
                    }
                }
                if (isFilter) {
                    element.getParent().remove(element);
                }
            }
            return document.asXML();
        } catch (DocumentException e) {
            throw new XmlMessageHandlerException("XML解析异常", e);
        }
    }

    @Override
    public String getType() {
        return "xmlFilter";
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Map<String, String> getConditionMap() {
        return conditionMap;
    }

    public void setConditionMap(Map<String, String> conditionMap) {
        this.conditionMap = conditionMap;
    }


}
