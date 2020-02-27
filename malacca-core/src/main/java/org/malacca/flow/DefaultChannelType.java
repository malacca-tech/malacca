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
public class DefaultChannelType implements ChannelType {

    /**
     * 是否是同步
     */
    private boolean isSynchronized = true;
    /**
     * 是否是异常通道
     */
    private boolean isExceptionType = false;

    @Override
    public boolean isSynchronized() {
        return isSynchronized;
    }

    @Override
    public boolean isExceptionType() {
        return isExceptionType;
    }

    public void setSynchronized(boolean aSynchronized) {
        isSynchronized = aSynchronized;
    }

    public void setExceptionType(boolean exceptionType) {
        isExceptionType = exceptionType;
    }
}
