package org.malacca.entry.holder;

import org.malacca.entry.Poller;
import org.malacca.entry.register.SpringEntryRegister;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/5
 * </p>
 * <p>
 * Department :
 * </p>
 */
@Component
public class PollerEntryHolder extends AbstractPollerEntryHolder {

    private static final Logger LOG = LoggerFactory.getLogger(PollerEntryHolder.class);

    @Autowired
    private SchedulerFactoryBean factoryBean;

    public PollerEntryHolder() {
    }

    private void register(String jobId, String jobName, Poller poller, String cron) throws Exception {
        register(jobId, jobName, poller, cron, new HashMap<>());
    }

    @Override
    public void loadEntry(String id, Poller entry) {
        LOG.info("注册轮询任务:{},表达式为:{}", id, entry.getCron());
        try {
            register(id, id, entry, entry.getCron());
        } catch (Exception e) {
            LOG.error("注册轮询任务失败:{},表达式为:{}", id, entry.getCron(), e);
            // TODO: 2020/3/5
            e.printStackTrace();
        }
        super.loadEntry(id, entry);
    }

    @Override
    public void unloadEntry(String id, Poller entry) {
        LOG.info("注销轮询任务:{}", id);
        try {
            unRegister(id);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: 2020/3/5
        }
    }

    private void unRegister(String jobId) throws Exception {
        Scheduler scheduler = factoryBean.getScheduler();
        try {
            for (JobKey jobKey : scheduleList(jobId, scheduler)) {
                JobDetailImpl jobDetail = (JobDetailImpl) scheduler.getJobDetail(jobKey);
                if (scheduler.checkExists(jobKey)) {
                    scheduler.interrupt(jobKey);
                    scheduler.deleteJob(jobKey);
                }
            }
        } catch (SchedulerException e) {
            // TODO: 2020/3/5
            throw new Exception("删除定时器失败" + jobId, e);
        }
    }

    private void register(String jobId, String jobName, Poller poller, String cron, HashMap<String, Object> jobDataMap) throws Exception {
        JobDetail jobDetail = getJobDetail(jobId, jobName, poller, jobDataMap);
        CronTrigger trigger = getCronTrigger(cron, jobDetail, jobId);
        try {
            Scheduler scheduler = factoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobId);
            boolean exists = scheduler.checkExists(jobKey);
            if (exists) {
                scheduler.deleteJob(jobKey);
            }
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            // TODO: 2020/3/5
            throw new Exception("启动定时器失败", e);
        }
    }

    // TODO: 2020/3/8 能不能通过spring的bean注册来完成这些初始化
    private JobDetail getJobDetail(String jobId, String jobName, Poller poller, HashMap<String, Object> jobDataMap) {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setName(jobId);
        jobDetailFactoryBean.setJobClass(PollerWrapper.class);
        JobDataMap jobDataMapInstance = new JobDataMap(jobDataMap);
        jobDetailFactoryBean.setJobDataMap(jobDataMapInstance);
        jobDetailFactoryBean.afterPropertiesSet();
        return jobDetailFactoryBean.getObject();
    }

    private CronTrigger getCronTrigger(String cron, JobDetail jobDetail, String jobId) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setCronExpression(cron);
        cronTriggerFactoryBean.setJobDetail(jobDetail);
        cronTriggerFactoryBean.setName(jobId);
        try {
            cronTriggerFactoryBean.afterPropertiesSet();
        } catch (ParseException e) {
            e.printStackTrace();
            // TODO: 2020/3/5
        }
        return cronTriggerFactoryBean.getObject();
    }

    private List<JobKey> scheduleList(String jobId, Scheduler scheduler) {
        List schedulerList = new ArrayList();
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    JobDetailImpl jobDetail = (JobDetailImpl) scheduler.getJobDetail(jobKey);
                    if (jobId != null && jobDetail.getName().startsWith(jobId)) {
                        schedulerList.add(jobKey);
                    }
                }
            }
        } catch (Exception e) {
            return schedulerList;
        }
        return schedulerList;
    }
}
