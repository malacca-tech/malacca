package org.malacca.component.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.support.helper.AesHelper;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractCryptographyFilter extends AbstractAdvancedComponent {

    // $.headerKey
    private List<String> headersExprList = new ArrayList<>();

    // $.headerKey[0]
    private List<String> paramsExprList = new ArrayList<>();

    // /message || $.message || $.payload
    private List<String> bodyExprList = new ArrayList<>();

    // STRING || JSON || XML
    private String bodyType = "STRING";

    private String encryptType;

    private AesHelper.AesMode mode;

    private AesHelper.BadPadding badPadding;

    private String iv;

    private AesHelper.OutputType outputType;

    private String password;

    public AbstractCryptographyFilter(String id, String name) {
        super(id, name);
    }

    enum EncryptType {
        AES, BASE64, MD5
    }

    @Override
    protected Message doHandleMessage(Message<?> message) {
        try {
            List<String> headersAndParams = new ArrayList<>();
            headersAndParams.addAll(headersExprList);
            headersAndParams.addAll(paramsExprList);
            for (String expr : headersAndParams) {
                Object data = JSONPath.eval(message.getContext(), expr);
                String newData = handleByType(String.valueOf(data));
                JSONPath.set(message.getContext(), expr, newData);
            }
            Message resultMessage = handleBody(message);
            return resultMessage;
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new MessagingException(encryptType + "数据加解密过滤器处理失败", e);
        }
    }

    private String handleByType(String data) throws Exception {
        EncryptType type = EncryptType.valueOf(encryptType);
        String result = "";
        switch (type) {
            case AES:
                result = handleAes(data);
                break;
            case BASE64:
                result = handleBase64(data);
                break;
            case MD5:
                result = handleMd5(data);
                break;
            default:
                throw new RuntimeException("未知的加解密类型" + encryptType);
        }
        return result;
    }

    private Message handleBody(Message<?> message) throws Exception {
        String payloadStr = String.valueOf(message.getPayload());
        MessageBuilder messageBuilder = MessageBuilder.fromMessage(message);

        for (String expr : bodyExprList) {
            if ("$.payload".equals(expr) || "STRING".equals(bodyType)) {
                String newData = handleByType(payloadStr);
                messageBuilder = MessageBuilder.withPayload(newData);
                continue;
            }
            if ("JSON".equals(bodyType)) {
                try {
                    Object payloadJson = JSON.parse(payloadStr);
                    Object pathResultObj = JSONPath.eval(payloadJson, expr);
                    if (pathResultObj instanceof JSONArray) {
                        JSONArray resultArray = (JSONArray) pathResultObj;
                        this.handleJSONArray(resultArray, expr, payloadJson);
                    } else {
                        String newData = handleByType(String.valueOf(pathResultObj));
                        JSONPath.set(payloadJson, expr, newData);
                    }
                    messageBuilder = MessageBuilder.withPayload(payloadJson.toString());
                } catch (Exception e) {
                    throw new RuntimeException("JSON数据解析异常", e);
                }
            } else if ("XML".equals(bodyType)) {
                try {
                    Document document = DocumentHelper.parseText(payloadStr);
                    List<Element> nodes = document.selectNodes(expr);
                    for (Element element : nodes) {
                        String newData = handleByType(element.getText());
                        element.setText(newData);
                    }
                    messageBuilder = MessageBuilder.withPayload(document.asXML());
                } catch (Exception e) {
                    throw new RuntimeException("XML数据解析异常", e);
                }
            }
        }
        return messageBuilder.copyContext(message.getContext()).build();
    }

    private void handleJSONArray(JSONArray dataArray, String jsonPath, Object payloadJson) throws Exception {
        Map<String, Object> paths = JSONPath.paths(payloadJson);
        Pattern pattern = Pattern.compile("/[0-9]+/");
        for (int i = 0; i < dataArray.size(); i++) {
            Object resultOne = dataArray.get(i);
            String result = handleByType(String.valueOf(resultOne));

            for (Map.Entry<String, Object> entry : paths.entrySet()) {
                String path = entry.getKey();
                Object value = entry.getValue();
                if (null != value
                        && value.equals(resultOne)
                        && jsonPath.equals("$" + path.replaceAll("/[0-9]+/", ".").replace("/", "."))) {
                    Matcher matcher = pattern.matcher(path);
                    String tmpPath = path;
                    while (matcher.find()) {
                        String group = matcher.group();
                        tmpPath = tmpPath.replace(group, "[" + group.replace("/", "") + "].");
                    }
                    tmpPath = "$" + tmpPath.replace("/", ".");
                    JSONPath.set(payloadJson, tmpPath, result);
                }
            }
        }
    }

    abstract protected String handleAes(String data) throws Exception;

    abstract protected String handleBase64(String data);

    abstract protected String handleMd5(String data);

    public List<String> getHeadersExprList() {
        return headersExprList;
    }

    public void setHeadersExprList(List<String> headersExprList) {
        this.headersExprList = headersExprList;
    }

    public List<String> getParamsExprList() {
        return paramsExprList;
    }

    public void setParamsExprList(List<String> paramsExprList) {
        this.paramsExprList = paramsExprList;
    }

    public List<String> getBodyExprList() {
        return bodyExprList;
    }

    public void setBodyExprList(List<String> bodyExprList) {
        this.bodyExprList = bodyExprList;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
    }

    public AesHelper.AesMode getMode() {
        return mode;
    }

    public void setMode(AesHelper.AesMode mode) {
        this.mode = mode;
    }

    public AesHelper.BadPadding getBadPadding() {
        return badPadding;
    }

    public void setBadPadding(AesHelper.BadPadding badPadding) {
        this.badPadding = badPadding;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public AesHelper.OutputType getOutputType() {
        return outputType;
    }

    public void setOutputType(AesHelper.OutputType outputType) {
        this.outputType = outputType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
