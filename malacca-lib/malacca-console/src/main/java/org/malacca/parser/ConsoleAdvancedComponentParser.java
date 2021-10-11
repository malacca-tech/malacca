package org.malacca.parser;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.ConsoleAdvancedComponent;
import org.malacca.definition.ComponentDefinition;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/8/14
 * </p>
 * <p>
 * Department :
 * </p>
 */
@ParserInterface(type = "component", typeAlia = "console")
public class ConsoleAdvancedComponentParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        ConsoleAdvancedComponent component = new ConsoleAdvancedComponent(definition.getId(), definition.getName());
        return component;
    }
}
