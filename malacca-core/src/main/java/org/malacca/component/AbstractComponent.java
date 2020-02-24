package org.malacca.component;

import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 此类为定义中间业务流程组件以及输出组件的抽象类
 * </p>
 * <p>
 * Author :chensheng 2020/2/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public abstract class AbstractComponent implements Component {
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

    public String getId() {
        return id;
    }

    @Override
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
}
