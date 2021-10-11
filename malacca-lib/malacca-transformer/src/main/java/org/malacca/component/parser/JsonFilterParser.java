package org.malacca.component.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.filter.JsonFilter;
import org.malacca.definition.ComponentDefinition;
import org.malacca.parser.AdvancedComponentParser;
import org.malacca.parser.ParserInterface;

import java.util.Map;

@ParserInterface(type = "component", typeAlia = "jsonFilter")
public class JsonFilterParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        JsonFilter jsonFilter = new JsonFilter(definition.getId(), definition.getName());
        return jsonFilter;
    }

    public void setConditionMap(JsonFilter component, Object condition) {
        Assert.isInstanceOf(Map.class, condition, "the conditionMap is not a map!");
        Map<String, String> conditionMap = (Map<String, String>) condition;
        component.setConditionMap(conditionMap);
    }
}
