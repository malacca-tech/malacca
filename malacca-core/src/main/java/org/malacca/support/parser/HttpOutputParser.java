package org.malacca.support.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.component.Component;
import org.malacca.component.output.HttpOutComponent;
import org.malacca.definition.ComponentDefinition;
import org.malacca.entry.HttpEntry;

import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/24
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class HttpOutputParser implements ComponentParser {
    private static final String URL_KEY = "url";
    public static final String TIMEOUT_KEY = "timeout";

    @Override
    public Component createInstance(ComponentDefinition definition) {
        HttpOutComponent component = new HttpOutComponent(definition.getId(), definition.getName());
        component.setStatus(definition.isStatus());
        component.setEnv(definition.getEnv());
        Map<String, Object> params = definition.getParams();
        setUrl(component, params.get(URL_KEY));
        setTimeout(component, params.get(TIMEOUT_KEY));
        //todo set other attribute】
        return component;
    }

    private void setUrl(HttpOutComponent component, Object url) {
        Assert.isInstanceOf(String.class, url, "the url is not a string!");
        String urlStr = (String) url;
        Assert.notBlank(urlStr, "url cannot be blank");
        component.setUrl(urlStr);
    }

    private void setTimeout(HttpOutComponent component, Object timeout) {
        Assert.isInstanceOf(String.class, timeout, "the timeout is not a string!");
        String timeoutStr = (String) timeout;
        Assert.state(isNumber(timeoutStr), "the timeout is not a number format!");
        Long timeoutLong = Long.valueOf((String) timeoutStr);
        component.setTimeout(timeoutLong);
    }

    private boolean isNumber(String str) {
        try {
            Long.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
