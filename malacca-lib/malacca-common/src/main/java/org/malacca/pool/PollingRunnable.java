package org.malacca.pool;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :yangxing 2021/5/14
 * </p>
 * <p>
 * Department :
 * </p>
 */
public abstract class PollingRunnable implements Runnable {

    /**
     * 是否成功获取Semaphore许可
     */
    private boolean isAcquire;

    private String name;

    public PollingRunnable(String name) {
        this.name = name;
    }

    public boolean isAcquire() {
        return isAcquire;
    }

    public void setAcquire(boolean acquire) {
        isAcquire = acquire;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

