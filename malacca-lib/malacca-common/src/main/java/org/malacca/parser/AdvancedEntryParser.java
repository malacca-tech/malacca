package org.malacca.parser;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.malacca.definition.EntryDefinition;
import org.malacca.entry.AbstractAdvancedEntry;
import org.malacca.entry.AbstractEntry;
import org.malacca.entry.Entry;
import org.malacca.support.parser.EntryParser;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
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
 * Author :chensheng 2020/8/16
 * </p>
 * <p>
 * Department :
 * </p>
 */
public abstract class AdvancedEntryParser implements EntryParser {

    private static final String APP_ID_KEY = "appId";

    @Override
    public Entry createInstance(EntryDefinition definition) {
        AbstractAdvancedEntry entry = doCreateInstance(definition);
        entry.setStatus(definition.isStatus());
        entry.setEnv(definition.getEnv());
        Map<String, Object> params = definition.getParams();
        this.setApp(entry, params.get(APP_ID_KEY));
        this.setParams(entry, params);
        return entry;
    }

    public void setParams(AbstractAdvancedEntry entry, Map<String, Object> params) {
        Class<?> entryClass = entry.getClass();
        if (null != params) {
            for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
                String fieldName = paramEntry.getKey();
                Object value = paramEntry.getValue();
                Method entryMethod;
                Class<?> fieldType;
                try {
                    try {
                        PropertyDescriptor descriptor = new PropertyDescriptor(fieldName, entryClass);
                        entryMethod = descriptor.getWriteMethod();
                        fieldType = descriptor.getPropertyType();
                    } catch (Exception e) {
                        // 没有get/set方法
                        continue;
                    }
                    try {
                        Method parserMethod = this.getClass().getMethod(entryMethod.getName(), entryClass, Object.class);
                        parserMethod.invoke(this, entry, value);
                        continue;
                    } catch (Exception e) {
                    }
                    Assert.isInstanceOf(fieldType, value, "the " + fieldName + " is not a " + fieldType.getSimpleName() + "!");
                    if (StrUtil.isBlankIfStr(value)) {
                        throw new RuntimeException(fieldName + " cannot be blank");
                    }
                    entryMethod.invoke(entry, value);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public void setApp(AbstractAdvancedEntry component, Object app) {
        if (app != null) {
            Assert.isInstanceOf(String.class, app, "the app is not a string!");
            String appStr = (String) app;
            component.setApp(appStr);
        }
    }

    public abstract AbstractAdvancedEntry doCreateInstance(EntryDefinition definition);
}
