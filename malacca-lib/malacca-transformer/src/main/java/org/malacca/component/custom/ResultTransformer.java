package org.malacca.component.custom;

import com.alibaba.fastjson.JSONObject;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.support.helper.MessageFreeMarker;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/10/31
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class ResultTransformer extends AbstractAdvancedComponent {

    private String codeConfig;
    private String dataConfig;
    private String causeConfig;
    private String tipsConfig;
    private boolean xml;

    public ResultTransformer(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        try {
            MessageFreeMarker messageFreeMarker = new MessageFreeMarker(message);
            String code = messageFreeMarker.parseExpression(codeConfig);
            String data = messageFreeMarker.parseExpression(dataConfig);
            String tips = messageFreeMarker.parseExpression(tipsConfig);
            String cause = messageFreeMarker.parseExpression(causeConfig);
            FlowExecutePublisher.publishEvent(FlowExecuteCode.INFO_SYSTEM
                    , String.format("%s-FreeMarker配置:\n%s, \n解析codeConfig: %s \n==>\n 结果:%s, \n解析dataConfig: %s \n==>\n 结果:%s, \n解析tipsConfig: %s \n==>\n 结果:%s, \n解析causeConfig: %s \n==>\n 结果:%s"
                            , logContext.getServiceId(), messageFreeMarker.getMessageMap(), codeConfig, code, dataConfig, data, tipsConfig, tips, causeConfig, cause)
                    , logContext);
            if (!"1".equals(code)) {
                throw new MessagingException("业务返回异常:" + tips + "cause:" + cause);
            }
            JSONObject result = new JSONObject();
            result.put("code", code);
            result.put("data", data);
            result.put("tips", tips);
            result.put("cause", cause);
            Message resultMessage = MessageBuilder.withPayload(result).copyContext(message.getContext()).build();
            return resultMessage;
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new MessagingException("结果转换器处理失败", e);
        }
    }

    @Override
    public String getType() {
        return "result";
    }

    public String getCodeConfig() {
        return codeConfig;
    }

    public void setCodeConfig(String codeConfig) {
        this.codeConfig = codeConfig;
    }

    public String getDataConfig() {
        return dataConfig;
    }

    public void setDataConfig(String dataConfig) {
        this.dataConfig = dataConfig;
    }

    public String getCauseConfig() {
        return causeConfig;
    }

    public void setCauseConfig(String causeConfig) {
        this.causeConfig = causeConfig;
    }

    public String getTipsConfig() {
        return tipsConfig;
    }

    public void setTipsConfig(String tipsConfig) {
        this.tipsConfig = tipsConfig;
    }

    public boolean isXml() {
        return xml;
    }

    public void setXml(boolean xml) {
        this.xml = xml;
    }
}
