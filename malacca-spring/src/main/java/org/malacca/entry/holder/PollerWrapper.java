package org.malacca.entry.holder;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;

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
@DisallowConcurrentExecution
public class PollerWrapper extends QuartzJobBean implements InterruptableJob {

    private static final Logger LOG = LoggerFactory.getLogger(PollerWrapper.class);

    private boolean running = false;

    private Job job;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        String jobId = context.getJobDetail().getJobDataMap().getString("jobId");
        String jobName = context.getJobDetail().getJobDataMap().getString("jobName");
        HashMap<String, Object> values = new HashMap<>();
        values.put("jobId", jobId);
        //防止任务重新启动
        if (running) {
            return;
        }
        running = true;
        try {
            LOG.info("开始执行作业" + ",jobName=" + jobName);
            doJob(context);
        } finally {
            running = false;
        }
    }

    private void doJob(JobExecutionContext context) {
        JobDetailImpl jobDetail = (JobDetailImpl) context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String jobName = jobDataMap.getString("jobName");
        try {
            //运行任务
            jobDataMap.put("sieJob", job);
            this.job = (Job) jobDataMap.get("jobBean");
            this.job.execute(context);
            //更新任务结果
            LOG.info("执行作业成功" + ",jobName=" + jobName);
        } catch (Exception e) {
            LOG.error("执行作业失败" + ",jobName=" + jobName, e);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        if (job instanceof InterruptableJob) {
            ((InterruptableJob) job).interrupt();
        }
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
