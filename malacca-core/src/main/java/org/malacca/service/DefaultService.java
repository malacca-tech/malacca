package org.malacca.service;

import com.sun.tools.javac.util.Assert;
import org.malacca.component.Component;
import org.malacca.definition.ComponentDefinition;
import org.malacca.definition.EntryDefinition;
import org.malacca.entry.Entry;
import org.malacca.messaging.Message;
import org.malacca.support.parser.Parser;
import org.malacca.utils.BeanFactoryUtils;
import org.malacca.utils.YmlParserUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class DefaultService extends AbstractService {

    private String parserSetting = "parserSetting.yml";


    @Override
    public void loadFlow(String flowStr) {

    }

    @Override
    Parser getParserByType(String type) {
        // TODO: 2020/2/24 获取解析器
        InputStream inputStream = DefaultService.class.getClassLoader().getResourceAsStream(parserSetting);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        try {
            Map<String, Object> parserMap = YmlParserUtils.ymlToMap(inputStreamReader);
            String parserClassName = String.valueOf(parserMap.get(type));
            Assert.checkNonNull(parserClassName, "没找到" + type + "解析器");
            Class<?> parserClass = Class.forName(parserClassName);
            Parser parser = (Parser) parserClass.newInstance();
            return parser;
        } catch (Exception e) {
            // TODO: 2020/2/24 rizhi
            e.printStackTrace();
        }
        return null;
    }

    @Override
    void retryFrom(String componentId, Message message) {
        //根据流程获取下一个component
        List<String> nextComponents = getFlow().getNextComponents(componentId, message);
        //开始重发
        for (String nextComponent : nextComponents) {
            Component component = getComponentMap().get(nextComponent);
            component.handleMessage(message);
        }
    }

    @Override
    Component doLoadComponent(Parser<Component> parser, ComponentDefinition definition) {
        //组装 组件成员变量 map 以供 反射注入
        Field[] fields = ComponentDefinition.class.getDeclaredFields();
        Map<String, Object> params = definition.getParams();
        Map<String, Object> commonMap = getCommonMap(fields, definition, params);
        //解析器生成实例
        Component instance = parser.createInstance(commonMap);
        return instance;
    }

    @Override
    Entry doLoadEntry(Parser<Entry> parser, EntryDefinition definition) {
        //组装 组件成员变量 map 以供 反射注入
        Field[] fields = EntryDefinition.class.getDeclaredFields();
        Map<String, Object> params = definition.getParams();
        Map<String, Object> commonMap = getCommonMap(fields, definition, params);
        //解析器生成实例
        Entry instance = parser.createInstance(commonMap);
        return instance;
    }

    private Map<String, Object> getCommonMap(Field[] fields, Object definition, Map<String, Object> params) {
        for (Field field : fields) {
            if (!"params".equals(field.getName())) {
                field.setAccessible(true);
                try {
                    params.put(field.getName(), field.get(definition));
                } catch (Exception e) {
                    // TODO: 2020/2/24
                    e.printStackTrace();
                }
            }
        }
        return params;
    }
}
