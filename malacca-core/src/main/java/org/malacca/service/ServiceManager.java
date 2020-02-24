package org.malacca.service;

import org.malacca.exception.ServiceLoadException;

import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 管理各个 service 的生命周期
 * </p>
 * <p>
 * Author :chensheng 2020/2/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface ServiceManager {

    /**
     * 加载service 供ServiceProvider调用 loadErrorException
     */
    void loadService(String yml) throws ServiceLoadException;

    /**
     * 卸载Service
     */
    void unloadService(String serviceId);

    /**
     * 获取Service列表
     */
    Map<String, Service> getServices();
}
