package org.malacca;

import java.util.concurrent.*;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :yangxing 2021/5/13
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class SemaphoreTest {

    public static final Integer CORE_POOL_SIZE = 2;
    public static final Integer MAX_POOL_SIZE = 5;
    public static final Integer MAX_CASE_SIZE = 8;
    public static final Integer PERMITS = 12;
    public static final Semaphore semaphore = new Semaphore(PERMITS, true);
    public static ThreadPoolExecutor spareExecutor =
            new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE * 2, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(MAX_CASE_SIZE));
    public static ThreadPoolExecutor poolExecutor =
            new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 10, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(MAX_CASE_SIZE),
                    new ThreadPoolExecutor.CallerRunsPolicy()) {
                protected void beforeExecute(Thread t, Runnable r) {
                    semaphore.acquireUninterruptibly();
//                    System.out.println(semaphore.availablePermits() + "准备执行：" + ((ThreadTask) r).taskName);
//                    print(((ThreadTask) r).taskName, null);
                }

                protected void afterExecute(Runnable r, Throwable t) {
                    semaphore.release();
//                    System.out.println(semaphore.availablePermits() + "执行完毕：" + ((ThreadTask) r).taskName);
//                    print(((ThreadTask) r).taskName, null);
                }

                protected void terminated() {
                    System.out.println("线程池退出");
                }
            };

    public static void execute(Runnable runnable) {
//        semaphore.acquireUninterruptibly();
        poolExecutor.execute(runnable);
//        semaphore.release();

    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            try {
//                if (semaphore.availablePermits() < PERMITS) {
//                    semaphore.release();
//                }
                execute(new ThreadTask(finalI));
            } catch (Exception e) {
                print(finalI, e);
            } finally {
//                semaphore.acquireUninterruptibly();
//                semaphore.release();
            }
        }
    }

    public static void print(Integer taskName, Exception e) {
        int poolSize = poolExecutor.getPoolSize();
        BlockingQueue<Runnable> queue = poolExecutor.getQueue();
        int queueSize = queue.size();
        int ability = semaphore.availablePermits();
        int queueLen = semaphore.getQueueLength();
        System.out.println(String.format("Tread-%s，已用线程-%s，缓存队列%s, 信号灯%s-%s, 异常：%s",
                taskName, poolSize, queueSize, ability, queueLen, e));
    }

    static class ThreadTask implements Runnable {
        Integer taskName;

        public ThreadTask(Integer name) {
            this.taskName = name;
        }

        public void run() {
            System.out.println("Tread-" + taskName);
//                    semaphore.acquireUninterruptibly();
            print(taskName, null);
            try {
                System.out.println("Tread-" + taskName);
                System.out.println("Tread-" + taskName);
            } catch (Exception e) {
                e.printStackTrace();
            }
//                    semaphore.release();
            print(taskName, null);
        }
    }
}

