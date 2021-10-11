package org.malacca.parser.hl7;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.hl7.Hl7OutComponent;
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
 * Author :chensheng 2020/4/6
 * </p>
 * <p>
 * Department :
 * </p>
 */
@ParserInterface(type = "component", typeAlia = "hl7")
public class Hl7OutComponentParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        Hl7OutComponent component = new Hl7OutComponent(definition.getId(), definition.getName());
        return component;
    }
}
