package org.malacca.component.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.filter.XmlFilter;
import org.malacca.definition.ComponentDefinition;
import org.malacca.parser.AdvancedComponentParser;
import org.malacca.parser.ParserInterface;

import java.util.Map;

@ParserInterface(type = "component", typeAlia = "xmlFilter")
public class XmlFilterParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        XmlFilter xmlFilter = new XmlFilter(definition.getId(), definition.getName());
        return xmlFilter;
    }

    public void setConditionMap(XmlFilter component, Object condition) {
        Assert.isInstanceOf(Map.class, condition, "the conditionMap is not a map!");
        Map<String, String> conditionMap = (Map<String, String>) condition;
        component.setConditionMap(conditionMap);
    }
}
