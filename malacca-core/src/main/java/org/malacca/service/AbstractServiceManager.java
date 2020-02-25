package org.malacca.service;

import org.malacca.component.Component;
import org.malacca.definition.ComponentDefinition;
import org.malacca.definition.EntryDefinition;
import org.malacca.definition.ServiceDefinition;
import org.malacca.entry.Entry;
import org.malacca.entry.register.EntryRegister;
import org.malacca.exception.ServiceLoadException;
import org.malacca.messaging.Message;
import org.malacca.support.ParserFactory;
import org.malacca.support.parser.Parser;
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
     * 用于给Service获取翻译definition的工厂类
     */
    protected ParserFactory parserFactory;

    protected AbstractServiceManager(EntryRegister entryRegister, ParserFactory parserFactory) {
        this.entryRegister = entryRegister;
        this.parserFactory = parserFactory;
        this.serviceMap = new HashMap<>();
    }

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
        // TODO: 2020/2/25 这里会有很多需要对异常进捕获的地方 并且要派发加载失败的事件
        if (serviceDefinition != null) {
            Service service = buildServiceInstance(serviceDefinition);
            //加载入口组件
            List<EntryDefinition> entryDefinitions = serviceDefinition.getEntries();
            for (EntryDefinition entryDefinition : entryDefinitions) {
                Parser<Entry, EntryDefinition> parser = parserFactory.getParser(entryDefinition.getType(), Entry.class);
                Entry entry = parser.createInstance(entryDefinition);
                service.addEntry(entry);
                entryRegister.registerEntry(entry);
            }

            List<ComponentDefinition> componentDefinitions = serviceDefinition.getComponents();
            for (ComponentDefinition componentDefinition : componentDefinitions) {
                Parser<Component, ComponentDefinition> parser = parserFactory.getParser(componentDefinition.getType(), Component.class);
                Component component = parser.createInstance(componentDefinition);
                service.addComponent(component);
            }
            //todo 同理，应该是有一个默认的string -> flow 的parser？flowBuilder
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
                entryRegister.deregisterEntry(entry);
            }
        }
        getServiceMap().remove(serviceId);
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

    public ParserFactory getParserFactory() {
        return parserFactory;
    }

    public void setParserFactory(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }
}
