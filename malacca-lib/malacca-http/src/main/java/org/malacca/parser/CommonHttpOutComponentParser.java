package org.malacca.parser;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.malacca.component.CommonHttpOutComponent;
import org.malacca.component.Component;
import org.malacca.component.output.HttpOutComponent;
import org.malacca.definition.ComponentDefinition;
import org.malacca.support.parser.ComponentParser;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/2
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class CommonHttpOutComponentParser implements ComponentParser {

    private static final String URL_KEY = "url";
    private static final String TIMEOUT_KEY = "timeout";
    private static final String MEDIA_TYPE_KEY = "mediaType";
    private static final String METHOD_KEY = "method";
    private static final String HEADERS_KEY = "headers";
    private static final String PARAMETERS_KEY = "parameters";


    @Override
    public Component createInstance(ComponentDefinition definition) {
        CommonHttpOutComponent component = new CommonHttpOutComponent(definition.getId(), definition.getName());
        component.setStatus(definition.isStatus());
        component.setEnv(definition.getEnv());
        Map<String, Object> params = definition.getParams();
        setUrl(component, params.get(URL_KEY));
        setTimeout(component, params.get(TIMEOUT_KEY));
        setMediaTypeKey(component, params.get(MEDIA_TYPE_KEY));
        setMethod(component, params.get(METHOD_KEY));
        setHeaders(component, params.get(HEADERS_KEY));
        setParameters(component, params.get(PARAMETERS_KEY));
        return component;
    }

    private void setUrl(CommonHttpOutComponent component, Object url) {
        Assert.isInstanceOf(String.class, url, "the url is not a string!");
        String urlStr = (String) url;
        Assert.notBlank(urlStr, "url cannot be blank");
        component.setUrl(urlStr);
    }

    private void setTimeout(CommonHttpOutComponent component, Object timeout) {
        Assert.isInstanceOf(String.class, timeout, "the timeout is not a string!");
        String timeoutStr = (String) timeout;
        Assert.state(isNumber(timeoutStr), "the timeout is not a number format!");
        Integer timeoutInt = Integer.valueOf((String) timeoutStr);
        component.setTimeout(timeoutInt);
    }

    private void setMediaTypeKey(CommonHttpOutComponent component, Object mediaType) {
        Assert.isInstanceOf(String.class, mediaType, "this mediaType is not a string!");
        String mediaTypeStr = (String) mediaType;
        Assert.notBlank(mediaTypeStr, "mediaType cannot be blank");
        component.setMediaType(mediaTypeStr);
    }

    private void setMethod(CommonHttpOutComponent component, Object method) {
        Assert.isInstanceOf(String.class, method, "this method is not a string!");
        String methodStr = (String) method;
        Assert.notBlank(methodStr, "method cannot be blank");
        component.setMethod(methodStr);
    }

    private void setHeaders(CommonHttpOutComponent component, Object headers) {
        if (headers == null) {
            return;
        }
        if (Map.class.isAssignableFrom(headers.getClass())) {
            component.setHeaders((Map<String, String>) headers);
        } else if (StrUtil.isBlank(String.valueOf(headers))) {
            component.setHeaders(new HashMap<>());
        } else {
            Assert.state(true, "headers is not collect format");
        }
    }

    private void setParameters(CommonHttpOutComponent component, Object params) {
        if (params == null) {
            return;
        }
        if (Map.class.isAssignableFrom(params.getClass())) {
            component.setParameters((Map<String, Object>) params);
        } else if (StrUtil.isBlank(String.valueOf(params))) {
            component.setParameters(new HashMap<>());
        } else {
            Assert.state(true, "params is not collect format");
        }
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
