package org.malacca.service;

import org.malacca.component.Component;
import org.malacca.flow.FlowElement;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;


public class DefaultService extends AbstractService implements Listener {

    //todo 注入进来
    private ServiceManager serviceManager;

    @Override
    void retryFrom(String componentId, Message message) {
        //根据流程获取下一个component
        ComponentRunner messageRunner = getMessageRunner(componentId, message);
        ComponentFutureTask<Message> task = new ComponentFutureTask<Message>(messageRunner);
        try {
            serviceManager.getThreadExecutor().execute(task);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: 2020/2/27 异常
        }
    }

    @Override
    public Message<?> onfinish(String id, Message<?> message) {
        List<FlowElement> nextComponents = getFlow().getNextComponents(id, message);
        // TODO: 2020/2/27 路由
        //大于一个 组件 判定为异步操作
        if (nextComponents.size() > 1) {
            for (FlowElement nextComponent : nextComponents) {
                ComponentRunner messageRunner = getMessageRunner(nextComponent.getComponentId(), message);
                ComponentFutureTask<Message> task = new ComponentFutureTask<Message>(messageRunner);
                serviceManager.getThreadExecutor().execute(task);
            }
            //异步操作
            return MessageBuilder.success().build();
        } else if (nextComponents.size() == 1) {
            FlowElement flowElement = nextComponents.get(0);
            ComponentRunner messageRunner = getMessageRunner(flowElement.getComponentId(), message);
            ComponentFutureTask<Message> task = new ComponentFutureTask<Message>(messageRunner);
            try {
                if (flowElement.isSynchronized()) {
                    return (Message<?>) messageRunner.call();
                } else {
                    serviceManager.getThreadExecutor().execute(task);
                    return MessageBuilder.success().build();
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: 2020/2/27 异常
                return null;
            }
        } else {
            //最后个组件 直接返回
            return message;
        }
    }

    private ComponentRunner getMessageRunner(String component, Message message) {
        return new ComponentRunner(component, message);
    }


    public class ComponentRunner implements Callable {

        private String componentId;

        private Message<?> message;

        public ComponentRunner(String componentId, Message<?> message) {
            this.componentId = componentId;
            this.message = message;
        }

        @Override
        public Object call() throws Exception {
            Component component = getComponentMap().get(componentId);
            component.handleMessage(message);
            return message;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
    }

    public static class ComponentFutureTask<V> extends FutureTask<V> {

        private ComponentRunner runner;

        public ComponentFutureTask(Callable<V> callable) {
            super(callable);
            this.runner = (ComponentRunner) callable;
        }

        public ComponentRunner getRunner() {
            return runner;
        }
    }

}
