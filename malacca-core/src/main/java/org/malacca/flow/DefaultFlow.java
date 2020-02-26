package org.malacca.flow;

import cn.hutool.core.lang.Assert;
import org.malacca.messaging.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/26
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class DefaultFlow implements Flow {

    /**
     * 流程列表  component_id -> flowElement
     */
    private Map<String, List<FlowElement>> elementMap;

    public DefaultFlow() {
        elementMap = new HashMap<>();
    }

    @Override
    public List<FlowElement> getNextComponents(String componentId, Message message) {
        return elementMap.get(componentId);
    }

    @Override
    public void addFlowElement(FlowElement flowElement) {
        Assert.notNull(flowElement, "flowElement is null!");
        List<FlowElement> elements = elementMap.get(flowElement.getComponentId());
        if (elements != null && elements.size() > 0) {
            elements.add(flowElement.getNextElement());
        } else {
            List<FlowElement> nextElements = new ArrayList<>();
            nextElements.add(flowElement.getNextElement());
            elementMap.put(flowElement.getComponentId(), nextElements);
        }
    }
}
