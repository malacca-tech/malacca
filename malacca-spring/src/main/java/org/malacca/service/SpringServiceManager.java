package org.malacca.service;

import org.malacca.entry.holder.PollerEntryHolder;
import org.malacca.entry.register.SpringEntryRegister;
import org.malacca.event.entity.system.ServiceLoadFailedEvent;
import org.malacca.executor.DefaultFlowExecutor;
import org.malacca.flow.FlowElement;
import org.malacca.parser.CommonHttpEntryParser;
import org.malacca.parser.CommonHttpOutComponentParser;
import org.malacca.parser.SqlEntryParser;
import org.malacca.parser.SqlOutComponentParser;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.malacca.component.Component;
import org.malacca.definition.ComponentDefinition;
import org.malacca.definition.EntryDefinition;
import org.malacca.definition.ServiceDefinition;
import org.malacca.entry.Entry;
import org.malacca.entry.register.DefaultEntryRegister;
import org.malacca.entry.register.EntryRegister;
import org.malacca.exception.FlowBuildException;
import org.malacca.exception.ServiceLoadException;
import org.malacca.exception.constant.SystemExceptionCode;
import org.malacca.flow.DefaultFlowBuilder;
import org.malacca.flow.Flow;
import org.malacca.flow.FlowBuilder;
import org.malacca.messaging.Message;
import org.malacca.support.ClassNameParserFactory;
import org.malacca.support.ParserFactory;
import org.malacca.support.parser.Parser;
import org.malacca.utils.YmlParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
 * Author :chensheng 2020/3/4
 * </p>
 * <p>
 * Department :
 * </p>
 */
@org.springframework.stereotype.Component
public class SpringServiceManager extends AbstractServiceManager implements InitializingBean, DisposableBean {

    private static final Logger LOG = LoggerFactory.getLogger(SpringServiceManager.class);

    @Autowired
    SpringEntryRegister springEntryRegister;

    @Autowired
    ApplicationContext context;

    public SpringServiceManager() {
    }

    protected SpringServiceManager(EntryRegister entryRegister, ParserFactory parserFactory, FlowBuilder flowBuilder) {
        super(entryRegister, parserFactory, flowBuilder);
    }

    @Override
    public void loadService(String yml) throws ServiceLoadException {
        ServiceDefinition serviceDefinition;
        try {
            serviceDefinition = YmlParserUtils.ymlToDefinition(yml);
        } catch (IOException e) {
            throw new ServiceLoadException(SystemExceptionCode.MALACCA_100001_PARSER_ERROR, "解析服务失败", yml, e);
        }

        // TODO: 2020/2/21 线程问题
        if (serviceDefinition != null) {
            Service service;
            try {
                service = buildServiceInstance(serviceDefinition);
                LOG.info("开始加载服务{}", service.getServiceId());
                //加载入口组件
                List<EntryDefinition> entryDefinitions = serviceDefinition.getEntries();
                for (EntryDefinition entryDefinition : entryDefinitions) {
                    try {
                        Parser<Entry, EntryDefinition> parser = parserFactory.getParser(entryDefinition.getType(), Entry.class);
                        Entry entry = parser.createInstance(entryDefinition);
                        entry.setFlowExecutor(executor);//执行器注入到entry
                        entry.setEntryKey();
                        entryRegister.registerEntry(entry);
                        service.addEntry(entry);
                    } catch (ServiceLoadException e) {
                        context.publishEvent(new ServiceLoadFailedEvent(context, service));
                        throw e;
                    } catch (Exception e) {
                        context.publishEvent(new ServiceLoadFailedEvent(context, service));
                        throw new ServiceLoadException(SystemExceptionCode.MALACCA_100002_ENTRY_REGISTER_ERROR, "service:" + service.getServiceId() + "解析Entry异常", e);
                    }
                }

                //加载组件
                List<ComponentDefinition> componentDefinitions = serviceDefinition.getComponents();
                for (ComponentDefinition componentDefinition : componentDefinitions) {
                    try {
                        Parser<Component, ComponentDefinition> parser = parserFactory.getParser(componentDefinition.getType(), Component.class);
                        Component component = parser.createInstance(componentDefinition);
                        service.addComponent(component);
                    } catch (Exception e) {
                        context.publishEvent(new ServiceLoadFailedEvent(context, service));
                        throw new ServiceLoadException(SystemExceptionCode.MALACCA_100005_COMPONENT__ERROR, "service:" + service.getServiceId() + "解析Component异常", e);
                    }
                }

                //加载流程
                try {
                    Flow flow = flowBuilder.buildFlow(serviceDefinition.getFlow(), service.getEntryMap(), service.getComponentMap());
                    service.setFlow(flow);
                    initFlowExecutor(service, flow);//flow执行器初始化
                } catch (FlowBuildException e) {
                    context.publishEvent(new ServiceLoadFailedEvent(context, service));
                    throw new ServiceLoadException(SystemExceptionCode.MALACCA_100003_FLOW_ERROR, "service:" + service.getServiceId() + "解析flow流程异常", e);
                }
                getServiceMap().put(service.getServiceId(), service);
            } catch (Exception e) {
                throw new ServiceLoadException(SystemExceptionCode.MALACCA_100003_FLOW_ERROR, "解析服务流程异常", yml, e);
            }
            LOG.info("服务{}加载成功", service.getServiceId());
        } else {
            throw new ServiceLoadException(SystemExceptionCode.MALACCA_100001_PARSER_ERROR, "解析服务失败", yml);
        }

    }

    /**
     * 检验初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (entryRegister == null) {
//            SpringEntryRegister entryRegister = new SpringEntryRegister();
//            entryRegister.afterPropertiesSet();
            this.entryRegister = springEntryRegister;

        }

        if (parserFactory == null) {
            ClassNameParserFactory classNameParserFactory = new ClassNameParserFactory();
            classNameParserFactory.setTypeAlia("poller", SqlEntryParser.class.getName(), "entry");
            classNameParserFactory.setTypeAlia("poller", SqlOutComponentParser.class.getName(), "component");
            classNameParserFactory.setTypeAlia("http", CommonHttpEntryParser.class.getName(), "entry");
            classNameParserFactory.setTypeAlia("http", CommonHttpOutComponentParser.class.getName(), "component");
            parserFactory = classNameParserFactory;
        }

        if (flowBuilder == null) {
            flowBuilder = new DefaultFlowBuilder();
        }

        if (executor == null) {
            executor = new DefaultFlowExecutor();
        }

        if (serviceMap == null) {
            serviceMap = new HashMap<>();
        }

        if (threadExecutor == null) {
            threadExecutor = new ThreadPoolExecutor(5, 200,
                    10, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(10000));
        }
    }

    /**
     * 关闭线程池
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        if (threadExecutor != null) {
            threadExecutor.shutdown();
        }
    }
}
