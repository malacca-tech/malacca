package org.malacca.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/7/1
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class ThreadPool {

    private static Logger logger = LoggerFactory.getLogger(ThreadPool.class);

    public static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(50, 200, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(20000));

    public static final Integer MAX_POOL_SIZE = 200;
    public static final Integer MAX_CACHE_SIZE = 10000;
    public static final Semaphore semaphore = new Semaphore(MAX_CACHE_SIZE - 5);
    public static ThreadPoolExecutor pollingExecutor =
            new ThreadPoolExecutor(50, MAX_POOL_SIZE, 10L, TimeUnit.SECONDS
                    , new LinkedBlockingQueue<Runnable>(MAX_CACHE_SIZE)
                    , new ThreadPoolExecutor.CallerRunsPolicy()) {
                protected void beforeExecute(Thread t, Runnable r) {
                    if (r instanceof PollingRunnable && pollingExecutor.getQueue().size() > MAX_POOL_SIZE - 1) {
                        try {
                            semaphore.acquire();
                            ((PollingRunnable) r).setAcquire(true);
                        } catch (Exception e) {
                            logger.error(((PollingRunnable) r).getName() + "获取信号灯失败", e);
                        }
                    }
                }

                protected void afterExecute(Runnable r, Throwable t) {
                    if (r instanceof PollingRunnable && ((PollingRunnable) r).isAcquire()) {
                        semaphore.release();
                    }
                }
            };

    public static void execute(Runnable runnable) {
        poolExecutor.execute(runnable);
    }

    public static void execute(PollingRunnable runnable) {
        pollingExecutor.execute(runnable);
    }
}
