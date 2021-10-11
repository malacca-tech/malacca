package org.malacca.component.parser;

import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.xslt.XsltTransformer;
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
@ParserInterface(type = "component", typeAlia = "transformer")
public class XsltTransformerParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        XsltTransformer component = new XsltTransformer(definition.getId(), definition.getName());
        return component;
    }

}
