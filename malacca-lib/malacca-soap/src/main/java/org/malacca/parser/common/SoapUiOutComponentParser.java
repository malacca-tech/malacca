package org.malacca.parser.common;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.common.SoapUiOutComponent;
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
 * Author :chensheng 2020/4/3
 * </p>
 * <p>
 * Department :
 * </p>
 */
@ParserInterface(type = "component", typeAlia = "soapui")
public class SoapUiOutComponentParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        SoapUiOutComponent component = new SoapUiOutComponent(definition.getId(), definition.getName());
        return component;
    }
}
