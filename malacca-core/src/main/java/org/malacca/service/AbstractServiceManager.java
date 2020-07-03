package org.malacca.service;

import org.malacca.component.Component;
import org.malacca.definition.ComponentDefinition;
import org.malacca.definition.EntryDefinition;
import org.malacca.definition.ServiceDefinition;
import org.malacca.entry.Entry;
import org.malacca.entry.register.EntryRegister;
import org.malacca.exception.ServiceLoadException;
import org.malacca.executor.DefaultFlowExecutor;
import org.malacca.executor.Executor;
import org.malacca.flow.Flow;
import org.malacca.flow.FlowBuilder;
import org.malacca.support.ParserFactory;
import org.malacca.support.parser.Parser;
import org.malacca.utils.YmlParserUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    protected EntryRegister entryRegister;

    /**
     * 服务缓存
     */
    protected Map<String, Service> serviceMap;

    /**
     * 线程池
     */
    protected ThreadPoolExecutor threadExecutor;

    /**
     * 用于给Service获取翻译definition的工厂类
     */
    protected ParserFactory parserFactory;

    /**
     * 加载flow的builder
     */
    protected FlowBuilder flowBuilder;

    public AbstractServiceManager() {
    }

    protected AbstractServiceManager(EntryRegister entryRegister, ParserFactory parserFactory, FlowBuilder flowBuilder) {
        this.entryRegister = entryRegister;
        this.parserFactory = parserFactory;
        this.flowBuilder = flowBuilder;
        this.serviceMap = new HashMap<>();
        // TODO: 2020/2/27 线程池舒初始化
        threadExecutor = new ThreadPoolExecutor(5, 200,
                10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000));
    }

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
            Executor flowExecutor = new DefaultFlowExecutor();
            Service service = buildServiceInstance(serviceDefinition);
            //加载入口组件
            List<EntryDefinition> entryDefinitions = serviceDefinition.getEntries();
            for (EntryDefinition entryDefinition : entryDefinitions) {
                Parser<Entry, EntryDefinition> parser = parserFactory.getParser(entryDefinition.getType(), Entry.class);
                Entry entry = parser.createInstance(entryDefinition);
                entry.setFlowExecutor(flowExecutor);//执行器注入到entry
                service.addEntry(entry);
                entryRegister.registerEntry(entry);
            }

            //加载组件
            List<ComponentDefinition> componentDefinitions = serviceDefinition.getComponents();
            for (ComponentDefinition componentDefinition : componentDefinitions) {
                Parser<Component, ComponentDefinition> parser = parserFactory.getParser(componentDefinition.getType(), Component.class);
                Component component = parser.createInstance(componentDefinition);
                service.addComponent(component);
            }

            //加载流程
            Flow flow = flowBuilder.buildFlow(serviceDefinition.getFlow(), service.getEntryMap(), service.getComponentMap());
            service.setFlow(flow);
            initFlowExecutor(flowExecutor, service, flow);//flow执行器初始化
            getServiceMap().put(service.getServiceId(), service);
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
    protected Service buildServiceInstance(ServiceDefinition definition) {
        DefaultService defaultService = new DefaultService();
        defaultService.setServiceId(definition.getServiceId());
        defaultService.setNamespace(definition.getNamespace());
        defaultService.setDisplayName(definition.getDisplayName());
        defaultService.setDescription(definition.getDescription());
        defaultService.setVersion(definition.getVersion());
        defaultService.setEnv(definition.getEnv());
        // TODO: 2020/2/27 应该注入
        defaultService.setServiceManager(this);
        return defaultService;
    }

    public Map<String, Service> getServiceMap() {
        if (this.serviceMap == null) {
            this.serviceMap = new HashMap<>(16);
        }
        return serviceMap;
    }

    protected void initFlowExecutor(Executor executor, Service service, Flow flow) {
        executor.setComponentMap(service.getComponentMap());
        executor.setFlow(flow);
        executor.setPoolExecutor(threadExecutor);
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

    public void setFlowBuilder(FlowBuilder flowBuilder) {
        this.flowBuilder = flowBuilder;
    }
}
