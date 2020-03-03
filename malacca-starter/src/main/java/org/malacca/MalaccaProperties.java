package org.malacca;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
@ConfigurationProperties(prefix = "malacca")
public class MalaccaProperties {
    /**
     * provider 的服务配置路径
     */
    private String classPath;

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }
}
