package org.malacca.service;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 被继承时 应在初始化时 获取yml
 * </p>
 * <p>
 * Author :chensheng 2020/2/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public abstract class AbstractServiceProvider implements ServiceProvider {

    private ServiceManager serviceManager;

    // TODO: 2020/2/20 初始化时调用
    protected abstract void findYml();

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    @Override
    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
}
