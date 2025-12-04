package io.springbatch.spring_batch.service;

import io.springbatch.spring_batch.job.BatchJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final Scheduler scheduler;

    public void scheduleJob(String jobName, String cronExpression) throws SchedulerException {
        // Job이 이미 존재하는지 확인하고, 없으면 새로 생성
        JobKey jobKey = new JobKey(jobName);
        JobDetail jobDetail;
        if (!scheduler.checkExists(jobKey)) {
            jobDetail = JobBuilder.newJob(BatchJob.class)
                    .withIdentity(jobKey)
                    .storeDurably()
                    .build();
            scheduler.addJob(jobDetail, false);
        } else {
            jobDetail = scheduler.getJobDetail(jobKey);
        }

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobName + "Trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        scheduler.scheduleJob(trigger);
    }

    public void updateJob(String jobName, String cronExpression) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(jobName + "Trigger");
        Trigger oldTrigger = scheduler.getTrigger(triggerKey);

        if (oldTrigger == null) {
            throw new SchedulerException("해당 Job에 대한 Trigger를 찾을 수 없습니다: " + jobName);
        }

        Trigger newTrigger = TriggerBuilder.newTrigger()
                .forJob(oldTrigger.getJobKey())
                .withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        scheduler.rescheduleJob(triggerKey, newTrigger);
    }

    public void deleteJob(String jobName) throws SchedulerException {
        scheduler.deleteJob(new JobKey(jobName));
    }

    /**
     * 특정 Job을 즉시 실행합니다.
     * @param jobName 실행할 Job의 이름
     * @throws SchedulerException Job이 존재하지 않거나 실행에 실패할 경우
     */
    public void triggerJob(String jobName) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerException("실행할 Job을 찾을 수 없습니다: " + jobName);
        }
        scheduler.triggerJob(jobKey);
    }
}
