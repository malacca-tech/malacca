package org.malacca.support.parser;

import java.util.Map;

/**
 *
 */
public interface Parser<T, K> {
    /**
     * 根据K的定义，获取T的实例
     *
     * @param definition
     * @return
     */
    T createInstance(K definition);
}
