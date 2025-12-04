package io.springbatch.spring_batch.controller;

import io.springbatch.spring_batch.dto.JobStatus;
import io.springbatch.spring_batch.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.batch.core.Job;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scheduler")
@RequiredArgsConstructor
public class SchedulerController {

    private final Scheduler scheduler;
    private final SchedulerService schedulerService;
    private final ApplicationContext applicationContext;

    @GetMapping("/job-names")
    public ResponseEntity<List<String>> getJobNames() {
        String[] jobNames = applicationContext.getBeanNamesForType(Job.class);
        return ResponseEntity.ok(Arrays.asList(jobNames));
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobStatus>> getJobs(@RequestParam(required = false) String jobName) throws SchedulerException {
        List<JobStatus> jobStatuses = new ArrayList<>();
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                String currentJobName = jobKey.getName();
                Trigger trigger = scheduler.getTriggersOfJob(jobKey).get(0);
                String cronExpression = ((CronTrigger) trigger).getCronExpression();
                jobStatuses.add(new JobStatus(currentJobName, cronExpression));
            }
        }

        // jobName 파라미터가 있으면 필터링
        if (StringUtils.hasText(jobName)) {
            return ResponseEntity.ok(jobStatuses.stream()
                    .filter(status -> status.getJobName().toLowerCase().contains(jobName.toLowerCase()))
                    .collect(Collectors.toList()));
        }

        return ResponseEntity.ok(jobStatuses);
    }

    @PostMapping("/jobs")
    public ResponseEntity<String> addSchedule(@RequestBody JobStatus jobStatus) {
        try {
            schedulerService.scheduleJob(jobStatus.getJobName(), jobStatus.getCronExpression());
            return ResponseEntity.ok("스케줄이 성공적으로 등록되었습니다.");
        } catch (SchedulerException e) {
            return ResponseEntity.badRequest().body("스케줄 등록에 실패했습니다: " + e.getMessage());
        }
    }

    @PutMapping("/jobs/{jobName}")
    public ResponseEntity<String> updateSchedule(@PathVariable String jobName, @RequestBody JobStatus jobStatus) {
        try {
            schedulerService.updateJob(jobName, jobStatus.getCronExpression());
            return ResponseEntity.ok("스케줄이 성공적으로 수정되었습니다.");
        } catch (SchedulerException e) {
            return ResponseEntity.badRequest().body("스케줄 수정에 실패했습니다: " + e.getMessage());
        }
    }

    @DeleteMapping("/jobs/{jobName}")
    public ResponseEntity<String> deleteSchedule(@PathVariable String jobName) {
        try {
            schedulerService.deleteJob(jobName);
            return ResponseEntity.ok("스케줄이 성공적으로 삭제되었습니다.");
        } catch (SchedulerException e) {
            return ResponseEntity.badRequest().body("스케줄 삭제에 실패했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/jobs/{jobName}/trigger")
    public ResponseEntity<String> triggerJob(@PathVariable String jobName) {
        try {
            schedulerService.triggerJob(jobName);
            return ResponseEntity.ok("'" + jobName + "' Job을 즉시 실행했습니다.");
        } catch (SchedulerException e) {
            return ResponseEntity.badRequest().body("Job 실행에 실패했습니다: " + e.getMessage());
        }
    }
}
