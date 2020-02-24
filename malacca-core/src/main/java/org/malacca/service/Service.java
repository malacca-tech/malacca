package org.malacca.service;

import org.malacca.definition.ComponentDefinition;
import org.malacca.definition.EntryDefinition;
import org.malacca.entry.Entry;
import org.malacca.entry.register.EntryRegister;

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

    /**
     * entry 注册器
     *
     * @param register
     */
    void setEntryRegister(EntryRegister register);

    /**
     * 加载组件
     * definition 组件参数定义
     * type 组件类型 根据类型 判断使用哪种解析器
     */
    void loadComponent(ComponentDefinition definition, String type);

    /**
     * 加载入口组件
     *
     * @param definition 组件参数定义
     * @param type       组件类型 根据类型 判断使用哪种holder
     */
    void loadEntry(EntryDefinition definition, String type);

    /**
     * 卸载入口组件
     */
    void unloadEntry(Entry entry);

    /**
     * 加载流程 使用FlowBuilder 闯将Flow
     */
    void loadFlow(String flowStr);

    /**
     * 获取entry缓存
     *
     * @return
     */
    Map<String, Entry> getEntryMap();
}
