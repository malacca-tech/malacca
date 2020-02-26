package org.malacca.service;

import org.malacca.messaging.Message;


public class DefaultService extends AbstractService {

    @Override
    void retryFrom(String componentId, Message message) {
        //根据流程获取下一个component
//        List<String> nextComponents = getFlow().getNextComponents(componentId, message);
//        //开始重发
//        for (String nextComponent : nextComponents) {
//            Component component = getComponentMap().get(nextComponent);
//            component.handleMessage(message);
//        }
    }

}
