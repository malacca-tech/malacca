package org.malacca.support.parser;

import org.malacca.utils.BeanFactoryUtils;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class AbstractParser<T> implements Parser<T> {

    public AbstractParser() {
    }

    /**
     * 要创建的实例的className
     */
    private String className;

    public AbstractParser(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 通用的 解析器
     */
    @Override
    public T createInstance(Map<String, Object> params) {
        Class cl = null;
        try {
            cl = Class.forName(className);
        } catch (ClassNotFoundException e) {
            // TODO: 2020/2/21 rizhi
        }
        Object newInstance = BeanFactoryUtils.newInstance(cl);
        Field[] fields = BeanFactoryUtils.getAllFields(cl);
        for (Field field : fields) {
            BeanFactoryUtils.setField(newInstance, field, params.get(field.getName()));
        }
        return (T) newInstance;
    }
}
