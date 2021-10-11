package org.malacca.entry;

import org.quartz.Job;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/15
 * </p>
 * <p>
 * Department :
 * </p>
 */
public abstract class AbstractAdvancedPollingEntry extends AbstractAdvancedEntry implements Poller {

    protected AbstractAdvancedPollingEntry(String id, String name) {
        super(id, name);
    }

    private String cron;

    public String getCron() {
        return this.cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public abstract void execute();

}
