package org.malacca.component.parser;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.js.JavaScriptTransformer;
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
@ParserInterface(type = "component", typeAlia = "js")
public class JavaScriptTransformerParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        JavaScriptTransformer component = new JavaScriptTransformer(definition.getId(), definition.getName());
        return component;
    }
}
