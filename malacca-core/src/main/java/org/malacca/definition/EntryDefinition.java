package org.malacca.definition;

import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/22
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class EntryDefinition {
    /**
     * 组件类型，根据此type 找寻解析类
     */
    private String type;
    private String id;
    private String name;
    /**
     * 判断此组件是否启用
     */
    private boolean status;

    /**
     * 组件内部使用的环境变量
     */
    private Map<String, String> env;

    /**
     * 组件内部使用的参数
     */
    private Map<String, Object> params;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public void setEnv(Map<String, String> env) {
        this.env = env;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
