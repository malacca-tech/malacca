package org.malacca.entry;

import org.quartz.Job;

public abstract class AbstractPollingEntry extends AbstractEntry implements Poller, Job {

    private String cron;

    protected AbstractPollingEntry(String id, String name) {
        super(id, name);
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
