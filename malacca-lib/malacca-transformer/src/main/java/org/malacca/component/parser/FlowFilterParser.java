package org.malacca.component.parser;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.filter.FlowFilter;
import org.malacca.definition.ComponentDefinition;
import org.malacca.parser.AdvancedComponentParser;
import org.malacca.parser.ParserInterface;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/15
 * </p>
 * <p>
 * Department :
 * </p>
 */
@ParserInterface(type = "component", typeAlia = "flowFilter")
public class FlowFilterParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        FlowFilter component = new FlowFilter(definition.getId(), definition.getName());
        return component;
    }
}
