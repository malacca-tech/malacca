package org.malacca.service;

import org.malacca.component.Component;
import org.malacca.entry.Entry;
import org.malacca.flow.Flow;
import org.malacca.messaging.Message;

import java.util.HashMap;
import java.util.Map;

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
public abstract class AbstractService implements Service{

    /**
     * 服务id
     */
    private String serviceId;
    /**
     * 服务描述
     */
    private String description;
    /**
     * 名称
     */
    private String displayName;
    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 版本号
     */
    private String version;

    /**
     * 组件通用环境变量
     */
    private Map<String, Object> env;

    /**
     * component
     */
    private Map<String, Component> componentMap = new HashMap<>();

    /**
     * entry
     * id->entry
     */
    private Map<String, Entry> entryMap = new HashMap<>();

    /**
     * 流程
     */
    private Flow flow;

    /**
     * 重发接口
     * componentId 组件id
     */
    abstract void retryFrom(String componentId, Message message);

    @Override
    public void unloadEntry(Entry entry) {
    }

    @Override
    public void addEntry(Entry entry) {
        getEntryMap().put(entry.getId(), entry);
    }

    @Override
    public void addComponent(Component component) {
        getComponentMap().put(component.getId(), component);
    }

    public String getServiceId() {
        return serviceId;
    }

    @Override
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Map<String, Component> getComponentMap() {
        return componentMap;
    }

    public void setComponentMap(Map<String, Component> componentMap) {
        this.componentMap = componentMap;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public Map<String, Object> getEnv() {
        return env;
    }

    public void setEnv(Map<String, Object> env) {
        this.env = env;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public Map<String, Entry> getEntryMap() {
        return entryMap;
    }
}
