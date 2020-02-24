package org.malacca.support.parser;

import java.util.Map;

/**
 * 编译
 */
public interface Parser<T> {
    /**
     * 根据参数 创建实例
     * @param params 实例的成员变量
     * @return
     * @throws ClassNotFoundException
     */
    T createInstance(Map<String, Object> params);
}
