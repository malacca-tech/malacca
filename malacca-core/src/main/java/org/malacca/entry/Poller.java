package org.malacca.entry;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 数据库输入使用 应该实现 Poller 和Job
 * </p>
 * <p>
 * Author :chensheng 2020/2/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface Poller extends Entry {
    /**
     * 获取时间表达式
     *
     * @return
     */
    String getCron();
}
