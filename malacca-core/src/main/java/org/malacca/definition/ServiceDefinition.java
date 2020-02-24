package org.malacca.definition;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: service 配置文件定义
 * </p>
 * <p>
 * Author :chensheng 2020/2/22
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class ServiceDefinition {
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

    private List<ComponentDefinition> components;

    private List<EntryDefinition> entries;

    private String flow;

    public String getServiceId() {
        return serviceId;
    }

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Object> getEnv() {
        return env;
    }

    public void setEnv(Map<String, Object> env) {
        this.env = env;
    }

    public List<ComponentDefinition> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentDefinition> components) {
        this.components = components;
    }

    public List<EntryDefinition> getEntries() {
        return entries;
    }

    public void setEntries(List<EntryDefinition> entries) {
        this.entries = entries;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }
}
