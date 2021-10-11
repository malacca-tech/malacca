package org.malacca.component.parser;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.freemarker.FreemarkerTransformer;
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
@ParserInterface(type = "component", typeAlia = "freemarker")
public class FreemarkerTransformerParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        FreemarkerTransformer component = new FreemarkerTransformer(definition.getId(), definition.getName());
        return component;
    }
}
