package org.malacca;

import org.malacca.entry.Entry;
import org.malacca.entry.holder.CommonHttpEntryHolder;
import org.malacca.entry.holder.EntryHolder;
import org.malacca.entry.holder.HttpEntryHolder;
import org.malacca.entry.register.DefaultEntryRegister;
import org.malacca.entry.register.EntryRegister;
import org.malacca.flow.DefaultFlowBuilder;
import org.malacca.flow.FlowBuilder;
import org.malacca.parser.CommonHttpEntryParser;
import org.malacca.parser.CommonHttpOutComponentParser;
import org.malacca.service.DefaultServiceManager;
import org.malacca.service.FileServiceProvider;
import org.malacca.service.ServiceManager;
import org.malacca.service.ServiceProvider;
import org.malacca.support.ClassNameParserFactory;
import org.malacca.support.ParserFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/3
 * </p>
 * <p>
 * Department :
 * </p>
 */
@EnableConfigurationProperties(MalaccaProperties.class)//开启使用映射实体对象
public class MalaccaAutoConfiguration {

    @Resource
    private MalaccaProperties properties;

    @Bean
    @ConditionalOnMissingBean(ServiceManager.class)
    public ServiceManager getServiceManager() {
        DefaultServiceManager commonServiceManager = new DefaultServiceManager(getEntryRegister(), getParserFactory(), getFlowBuilder());
        return commonServiceManager;
    }

    @Bean
    @ConditionalOnMissingBean(HttpEntryHolder.class)
    public EntryHolder<Entry> getHttpEntryHolder() {
        CommonHttpEntryHolder httpEntryHolder = new CommonHttpEntryHolder();
        return httpEntryHolder;
    }

    @Bean
    @ConditionalOnMissingBean(EntryRegister.class)
    public EntryRegister getEntryRegister() {
        DefaultEntryRegister defaultEntryRegister = new DefaultEntryRegister();
        defaultEntryRegister.putHolder("httpEntry", getHttpEntryHolder());
        return defaultEntryRegister;
    }

    @Bean
    @ConditionalOnMissingBean(ParserFactory.class)
    public ParserFactory getParserFactory() {
        ClassNameParserFactory classNameParserFactory = new ClassNameParserFactory();
        // TODO: 2020/3/3 如何从配置文件里面获取
        classNameParserFactory.setTypeAlia("http", CommonHttpEntryParser.class.getName(), "entry");
        classNameParserFactory.setTypeAlia("http", CommonHttpOutComponentParser.class.getName(), "component");
        return classNameParserFactory;
    }

    @Bean
    @ConditionalOnMissingBean(FlowBuilder.class)
    public FlowBuilder getFlowBuilder() {
        DefaultFlowBuilder defaultFlowBuilder = new DefaultFlowBuilder();
        return defaultFlowBuilder;
    }

    @Bean
    @ConditionalOnMissingBean(ServiceProvider.class)
    public ServiceProvider getServiceProvider() {
        FileServiceProvider fileServiceProvider = new FileServiceProvider();
        fileServiceProvider.setServiceManager(getServiceManager());
        fileServiceProvider.setPath(properties.getClassPath());
        fileServiceProvider.init();
        return fileServiceProvider;
    }

}
