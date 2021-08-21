package org.malacca.event;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/25
 * </p>
 * <p>
 * Department :
 * </p>
 */
public abstract class AbstractEventListener implements MalaccaEventListener, Runnable {

    private Thread thread;

    private BlockingQueue<Event> queue;

    private String queueName;

    private static final Integer BACH_SIZE = 10000;

    public AbstractEventListener(String queueName) {
        queue = new LinkedBlockingQueue(400000);
        thread = new Thread(this);
        this.queueName = queueName;
        thread.setName("malacca_" + queueName);
        thread.start();
    }

    public void run() {
        while (true) {
            try {
                if (queue != null) {
                    if (this.thread.isInterrupted()) {
                        throw new InterruptedException();
                    }
                    while (queue.size() > 0) {
                        processQueue();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: 2020/2/25  异常
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                // TODO: 2020/2/25  异常
            }
        }
    }

    protected int getQueueSize() {
        return queue.size();
    }

    private void processQueue() throws InterruptedException {
        List<Event> eventList = new ArrayList();
        int size = queue == null ? 0 : queue.size();
        int batchSize = size;
        if (size >= BACH_SIZE) {
            batchSize = BACH_SIZE;
        }
        for (int i = batchSize; i > 0; i--) {
            Event event = queue.take();
            eventList.add(event);
            if (queue == null || queue.size() == 0) {
                break;
            }
        }
        process(eventList);
    }

    @PreDestroy
    public void cacheShutDown() {
        thread.interrupt();
        queue.clear();
    }

    public void onEvent(Event event) {
        queue.add(event);
        if (!this.thread.isAlive()) {
            thread = new Thread(this);
            thread.setName("SIE_" + queueName);
            thread.start();
        }
    }

    public abstract void process(List<Event> eventList);

    public BlockingQueue getQueue() {
        return queue;
    }

}
