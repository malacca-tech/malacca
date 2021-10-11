package org.malacca.component.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.custom.ResultTransformer;
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
 * Author :chensheng 2020/10/31
 * </p>
 * <p>
 * Department :
 * </p>
 */
@ParserInterface(type = "component", typeAlia = "result")
public class ResultTransformerParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        ResultTransformer component = new ResultTransformer(definition.getId(), definition.getName());
        return component;
    }

    public void setXml(ResultTransformer component, Object xml) {
        Assert.isInstanceOf(String.class, xml, "xml  is not a string!");
        Boolean xmlBoolean = Boolean.valueOf(String.valueOf(xml));
        component.setXml(xmlBoolean);
    }
}
