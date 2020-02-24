package org.malacca.service;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:用于提供 serviceManger 使用的加载的yml
 * 调用 ServiceManager loadService(String yml) 方法 开始 解析服务
 * </p>
 * <p>
 * Author :chensheng 2020/2/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface ServiceProvider {

    /**
     *注入 ServiceManager
     */
    void setServiceManager(ServiceManager manager);
}
