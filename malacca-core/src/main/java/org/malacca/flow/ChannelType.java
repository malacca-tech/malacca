package org.malacca.flow;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/28
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface ChannelType {
    /**
     * 是否是同步
     *
     * @return
     */
    boolean isSynchronized();

    /**
     * 是否是异常通道
     *
     * @return
     */
    boolean isExceptionType();
}
