package org.malacca.component.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.event.FlowExecuteCode;
import org.malacca.event.FlowExecutePublisher;
import org.malacca.exception.JsonMessageHandlerException;
import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.support.helper.FreeMarker;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * JSON格式消息过滤
 */
public class JsonFilter extends AbstractAdvancedComponent {

    private String dataPath;
    private String jsonPath;
    private Map<String, String> conditionMap;

    public JsonFilter(String id, String name) {
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
            throw new JsonMessageHandlerException("JSON组件执行失败", e);
        }
    }

    public boolean isContinue(String resultPayload) {
        Object result = JSONPath.eval(JSON.parse(resultPayload), jsonPath);
        if (result instanceof List) {
            List children = (List) result;
            return !children.isEmpty();
        } else if (result instanceof JSONObject) {
            JSONObject data = (JSONObject) result;
            return !data.isEmpty();
        } else {
            throw new JsonMessageHandlerException("JSON消息过滤器处理返回数据不是JSON格式");
        }
    }

    public void filter(List<Map<String, Object>> dataList) {
        int i = 0;
        while (i < dataList.size()) {
            Map<String, Object> data = dataList.get(i);
            FreeMarker freeMarker = new FreeMarker();
            freeMarker.setMap(data);
            boolean isFilter = true;
            for (Map.Entry<String, String> entry : conditionMap.entrySet()) {
                String key = entry.getKey();
                String condition = entry.getValue();
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
                dataList.remove(data);
                continue;
            }
            i++;
        }
    }

    public String handleFreemarker(String payload) {
        if (!JSON.isValid(payload)) {
            throw new JsonMessageHandlerException("消息体不是JSON格式数据");
        }
        Object tmp = JSON.parse(payload);
        Object result = JSONPath.eval(tmp, jsonPath);
        if (result instanceof JSONArray) {
            this.filter((List<Map<String, Object>>) result);
        } else {
            this.filter(Arrays.asList((JSONObject) result));
        }
        return tmp.toString();
    }

    @Override
    public String getType() {
        return "jsonFilter";
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public Map<String, String> getConditionMap() {
        return conditionMap;
    }

    public void setConditionMap(Map<String, String> conditionMap) {
        this.conditionMap = conditionMap;
    }
}
