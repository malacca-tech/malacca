package org.malacca.support.parser;

import org.malacca.component.Component;
import org.malacca.component.output.HttpOutComponent;
import org.malacca.definition.ComponentDefinition;

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

    @Override
    public Component createInstance(ComponentDefinition definition) {
        HttpOutComponent component = new HttpOutComponent(definition.getId(),definition.getName());
        //todo set other attribute
        return component;
    }
}
