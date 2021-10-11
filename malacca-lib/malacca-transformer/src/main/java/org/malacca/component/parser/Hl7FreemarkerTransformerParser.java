package org.malacca.component.parser;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.freemarker.Hl7DocumentFreeMarkerTransformer;
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
@ParserInterface(type = "component", typeAlia = "hl7Freemarker")
public class Hl7FreemarkerTransformerParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        Hl7DocumentFreeMarkerTransformer component = new Hl7DocumentFreeMarkerTransformer(definition.getId(), definition.getName());
        return component;
    }
}
