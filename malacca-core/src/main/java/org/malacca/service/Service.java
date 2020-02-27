package org.malacca.service;

import org.malacca.component.Component;
import org.malacca.definition.ComponentDefinition;
import org.malacca.definition.EntryDefinition;
import org.malacca.entry.Entry;
import org.malacca.entry.register.EntryRegister;
import org.malacca.flow.Flow;

import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 加载组件，元数据管理， 编排消息传递，提供日志功能
 * </p>
 * <p>
 * Author :chensheng 2020/2/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface Service {
    /**
     * 服务id
     *
     * @param serviceId
     */
    void setServiceId(String serviceId);

    String getServiceId();

    /**
     * 加入入口
     *
     * @param entry
     */
    void addEntry(Entry entry);

    /**
     * 添加组件
     *
     * @param component
     */
    void addComponent(Component component);

    /**
     * 卸载入口组件
     */
    void unloadEntry(Entry entry);

    /**
     * 加载流程 使用FlowBuilder 闯将Flow
     */
    void setFlow(Flow flow);

    /**
     * 获取entry列表
     *
     * @return
     */
    Map<String, Entry> getEntryMap();

    /**
     * 获取组件列表
     *
     * @return
     */
    Map<String, Component> getComponentMap();

    /**
     * 获取流程
     * @return
     */
    Flow getFlow();
}
