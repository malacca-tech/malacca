package org.malacca.service;

import org.malacca.messaging.Message;


public class DefaultService extends AbstractService {

    //todo 注入进来
    private ServiceManager serviceManager;

    @Override
    void retryFrom(String componentId, Message message) {
        //根据流程获取下一个component
        try {
//            serviceManager.executor.execute(task);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: 2020/2/27 异常
        }
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
}
