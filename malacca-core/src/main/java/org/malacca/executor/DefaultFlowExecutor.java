package org.malacca.executor;

import org.malacca.component.Component;
import org.malacca.flow.Flow;
import org.malacca.flow.FlowElement;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/27
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class DefaultFlowExecutor implements Executor {

    private Flow flow;

    private ThreadPoolExecutor poolExecutor;

    private Map<String, Component> componentMap;

    public DefaultFlowExecutor() {
    }

    public DefaultFlowExecutor(Flow flow, ThreadPoolExecutor poolExecutor, Map<String, Component> componentMap) {
        this.flow = flow;
        this.poolExecutor = poolExecutor;
        this.componentMap = componentMap;
    }

    @Override
    public Message<?> execute(String componentId, Message<?> message) {
        List<FlowElement> nextElements = getFlow().getNextFlowElements(componentId, message);
        if (nextElements == null || nextElements.size() == 0) {
            return message;
        }

        // TODO: 2020/2/28 异常处理通道
        //大于一个 组件 判定为异步操作
        if (nextElements.size() > 1) {
            for (FlowElement nextElement : nextElements) {
                handleAsyncMessage(nextElement, message);
            }
            return MessageBuilder.success().build();
        } else {
            FlowElement nextElement = nextElements.get(0);
            if (nextElement.getType().isSynchronized()) {
                return this.handleSyncMessage(nextElement.getSufComponentId(), message);
            } else {
                handleAsyncMessage(nextElement, message);
                return MessageBuilder.success().build();
            }
        }
    }

    public void handleAsyncMessage(FlowElement element, Message<?> message) {
        DefaultFlowExecutor flowExecutor = new DefaultFlowExecutor(flow, poolExecutor, componentMap);
        FlowExecutorRunner runner = new FlowExecutorRunner(flowExecutor, element.getSufComponentId(), message);
        poolExecutor.execute(runner);
    }

    public Message<?> handleSyncMessage(String componentId, Message<?> message) {
        Component component = componentMap.get(componentId);
        Message handleMessage = component.handleMessage(message);
        return this.execute(componentId, handleMessage);
    }

    public static class FlowExecutorRunner implements Runnable {

        private DefaultFlowExecutor flowExecutor;

        private String componentId;

        private Message<?> message;

        public FlowExecutorRunner(DefaultFlowExecutor flowExecutor, String componentId, Message<?> message) {
            this.flowExecutor = flowExecutor;
            this.componentId = componentId;
            this.message = message;
        }

        @Override
        public void run() {
            flowExecutor.handleSyncMessage(componentId, message);
        }
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public ThreadPoolExecutor getPoolExecutor() {
        return poolExecutor;
    }

    public void setPoolExecutor(ThreadPoolExecutor poolExecutor) {
        this.poolExecutor = poolExecutor;
    }

    public Map<String, Component> getComponentMap() {
        return componentMap;
    }

    public void setComponentMap(Map<String, Component> componentMap) {
        this.componentMap = componentMap;
    }
}
