package org.malacca.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.definition.EntryDefinition;
import org.malacca.entry.CommonHttpEntry;
import org.malacca.entry.Entry;
import org.malacca.support.parser.EntryParser;

import java.util.Map;

public class CommonHttpEntryParser implements EntryParser {
    public static final String URI_KEY = "uri";
    public static final String METHOD_KEY = "method";
    public static final String PORT_KEY = "port";
    public static final String PAYLOAD_EXPRESSION_KEY = "payloadExpression";


    @Override
    public Entry createInstance(EntryDefinition definition) {
        CommonHttpEntry entry = new CommonHttpEntry(definition.getId(), definition.getName());
        entry.setStatus(definition.isStatus());
        entry.setEnv(definition.getEnv());
        Map<String, Object> params = definition.getParams();
        setPath(entry, params.get(URI_KEY));
        setMethod(entry, params.get(METHOD_KEY));
        setPayloadExpression(entry, params.get(PAYLOAD_EXPRESSION_KEY));
        setPort(entry, params.get(PORT_KEY));
        entry.setEntryKey();
        return entry;
    }

    private void setPath(CommonHttpEntry entry, Object uri) {
        Assert.isInstanceOf(String.class, uri, "the uri is not a string!");
        String uriStr = (String) uri;
        Assert.notBlank(uriStr, "uri cannot be blank");
        entry.setUri(uriStr);
    }

    private void setMethod(CommonHttpEntry entry, Object method) {
        Assert.isInstanceOf(String.class, method, "the method is not a string!");
        String methodStr = (String) method;
        Assert.notBlank(methodStr, "method cannot be blank");
        entry.setMethod(methodStr);
    }

    private void setPort(CommonHttpEntry entry, Object port) {
        Assert.isInstanceOf(String.class, port, "the port is not a string!");
        String portStr = (String) port;
        Assert.state(isNumber(portStr), "the port is not a number format!");
        Integer portInt = Integer.valueOf((String) portStr);
        entry.setPort(portInt);
    }

    private void setPayloadExpression(CommonHttpEntry entry, Object payloadExpression) {
        Assert.isInstanceOf(String.class, payloadExpression, "the payloadExpressionStr is not a string!");
        String payloadExpressionStr = (String) payloadExpression;
        Assert.notBlank(payloadExpressionStr, "payloadExpressionStr cannot be blank");
        entry.setPayloadExpression(payloadExpressionStr);
    }

    private boolean isNumber(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

