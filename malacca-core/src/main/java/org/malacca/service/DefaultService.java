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

    @Override
    public void loadFlow(String flowStr) {

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
