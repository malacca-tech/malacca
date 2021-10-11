package org.malacca.parser;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.Component;
import org.malacca.definition.ComponentDefinition;
import org.malacca.support.parser.ComponentParser;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/8/16
 * </p>
 * <p>
 * Department :
 * </p>
 */
public abstract class AdvancedComponentParser implements ComponentParser {

    private static final String APP_ID_KEY = "appId";
    public static final String RETRY_KEY = "retry";
    public static final String USE_ORIGIN_MESSAGE_KEY = "useOriginMessage";

    @Override
    public Component createInstance(ComponentDefinition definition) {
        AbstractAdvancedComponent component = doCreateInstance(definition);
        component.setStatus(definition.isStatus());
        component.setEnv(definition.getEnv());
        Map<String, Object> params = definition.getParams();
        this.setApp(component, params.get(APP_ID_KEY));
        this.setRetry(component, params.get(RETRY_KEY));
        this.setUseOriginMessage(component, params.get(USE_ORIGIN_MESSAGE_KEY));
        this.setParams(component, params);
        return component;
    }

    public void setParams(AbstractAdvancedComponent component, Map<String, Object> params) {
        Class<?> componentClass = component.getClass();
        if (null != params) {
            for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
                String fieldName = paramEntry.getKey();
                Object value = paramEntry.getValue();
                Method componentMethod;
                Class<?> fieldType;
                try {
                    try {
                        PropertyDescriptor descriptor = new PropertyDescriptor(fieldName, componentClass);
                        componentMethod = descriptor.getWriteMethod();
                        fieldType = descriptor.getPropertyType();
                    } catch (Exception e) {
                        // 没有get/set方法
                        continue;
                    }
                    try {
                        Method parserMethod = this.getClass().getMethod(componentMethod.getName(), componentClass, Object.class);
                        parserMethod.invoke(this, component, value);
                        continue;
                    } catch (Exception e) {
                    }
                    setDefault(component, componentMethod, fieldType, value, fieldName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void setApp(AbstractAdvancedComponent component, Object app) {
        if (app != null) {
            Assert.isInstanceOf(String.class, app, "the app is not a string!");
            String appStr = (String) app;
            component.setApp(appStr);
        }
    }

    public void setRetry(AbstractAdvancedComponent component, Object retry) {
        if (retry != null) {
            Assert.isInstanceOf(String.class, retry, "the retry is not a string!");
            String retryStr = (String) retry;
            component.setRetry(Boolean.parseBoolean(retryStr));
        }
    }

    public void setUseOriginMessage(AbstractAdvancedComponent component, Object useOriginMessage) {
        if (useOriginMessage != null) {
            Assert.isInstanceOf(String.class, useOriginMessage, "the useOriginMessage is not a string!");
            String useOriginMessageStr = (String) useOriginMessage;
            component.setUseOriginMessage(Boolean.parseBoolean(useOriginMessageStr));
        }
    }

    public void setDefault(AbstractAdvancedComponent component, Method method, Class<?> fieldType, Object value, String fieldName) throws InvocationTargetException, IllegalAccessException {
        switch (fieldType.getSimpleName()) {
            case "Boolean":
            case "boolean":
                method.invoke(component, Boolean.parseBoolean(String.valueOf(value)));
                break;
            case "Integer":
            case "int":
                String str = String.valueOf(value);
                Assert.state(isNumber(str), String.format("the %s is not a number format!", fieldName));
                Integer valueInt = Integer.valueOf(str);
                method.invoke(component, valueInt);
                break;
            case "String":
            default:
                Assert.isInstanceOf(fieldType, value, "the " + fieldName + " is not a " + fieldType.getSimpleName() + "!");
                if (StrUtil.isBlankIfStr(value)) {
                    throw new RuntimeException(fieldName + " cannot be blank");
                }
                method.invoke(component, value);
                break;
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

    public abstract AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition);
}
