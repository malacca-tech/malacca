package org.malacca.service;

import org.malacca.definition.ComponentDefinition;
import org.malacca.definition.EntryDefinition;
import org.malacca.definition.ServiceDefinition;
import org.malacca.entry.Entry;
import org.malacca.entry.register.EntryRegister;
import org.malacca.exception.ServiceLoadException;
import org.malacca.messaging.Message;
import org.malacca.utils.YmlParserUtils;

import java.io.IOException;
import java.util.HashMap;
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
 * Author :chensheng 2020/2/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public abstract class AbstractServiceManager implements ServiceManager {

    /**
     * entry 注册器 应该是注册进来
     */
    private EntryRegister entryRegister;

    /**
     * 服务缓存
     */
    private Map<String, Service> serviceMap;

    /**
     * 线程池
     */
    private ThreadPoolExecutor threadExecutor;


    /**
     * 提供重新发布功能
     * serviceId 服务id
     * componentId 组件id 用于定位从什么位置开始进行重推操作
     * message 请求消息
     */
    abstract void retryFrom(String serviceId, String componentId, Message message);

    /**
     * 1.创建service实例
     * 2.
     *
     * @param yml
     * @throws ServiceLoadException
     */
    @Override
    public void loadService(String yml) throws ServiceLoadException {
        ServiceDefinition serviceDefinition;
        try {
            serviceDefinition = YmlParserUtils.ymlToDefinition(yml);
        } catch (IOException e) {
            // TODO: 2020/2/21 log
            throw new ServiceLoadException("解析yml失败", e);
        }

        // TODO: 2020/2/21 线程问题
        if (serviceDefinition != null) {
            Service service = buildServiceInstance(serviceDefinition);
            service.setEntryRegister(entryRegister);
            //加载入口组件
            List<EntryDefinition> entryDefinitions = serviceDefinition.getEntries();
            for (EntryDefinition entryDefinition : entryDefinitions) {
                service.loadEntry(entryDefinition, entryDefinition.getType());
            }
            List<ComponentDefinition> componentDefinitions = serviceDefinition.getComponents();
            for (ComponentDefinition componentDefinition : componentDefinitions) {
                service.loadComponent(componentDefinition, componentDefinition.getType());
            }
            service.loadFlow(serviceDefinition.getFlow());
        }
    }

    @Override
    public void unloadService(String serviceId) {
        // TODO: 2020/2/24 rizhi
        Service service = getServiceMap().get(serviceId);
        if (service != null) {
            Map<String, Entry> entryMap = service.getEntryMap();
            for (Entry entry : entryMap.values()) {
                service.unloadEntry(entry);
            }
        }
    }

    @Override
    public Map<String, Service> getServices() {
        return this.serviceMap;
    }

    // TODO: 2020/2/21 yml 实体类
    private Service buildServiceInstance(ServiceDefinition definition) {
        DefaultService defaultService = new DefaultService();
        defaultService.setServiceId(definition.getServiceId());
        defaultService.setNamespace(definition.getNamespace());
        defaultService.setDisplayName(definition.getDisplayName());
        defaultService.setDescription(definition.getDescription());
        defaultService.setVersion(definition.getVersion());
        defaultService.setEnv(definition.getEnv());
        // TODO: 2020/2/24  应该是初始化的时候注入进去
        defaultService.setEntryRegister(entryRegister);
        return defaultService;
    }

    public Map<String, Service> getServiceMap() {
        if (this.serviceMap == null) {
            this.serviceMap = new HashMap<>();
        }
        return serviceMap;
    }

    // TODO: 2020/2/20 连接池初始化
    public ThreadPoolExecutor getThreadExecutor() {
        return threadExecutor;
    }

    public void setThreadExecutor(ThreadPoolExecutor threadExecutor) {
        this.threadExecutor = threadExecutor;
    }

    public EntryRegister getEntryRegister() {
        return entryRegister;
    }

    public void setEntryRegister(EntryRegister entryRegister) {
        this.entryRegister = entryRegister;
    }
}
