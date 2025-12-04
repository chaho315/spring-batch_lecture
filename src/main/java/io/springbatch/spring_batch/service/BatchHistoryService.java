package io.springbatch.spring_batch.service;

import io.springbatch.spring_batch.dto.JobExecutionDetailsDto;
import io.springbatch.spring_batch.dto.JobExecutionHistoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchHistoryService {

    private final JobExplorer jobExplorer;
    private static final int MAX_INSTANCES_PER_JOB = 100;

    public List<JobExecutionHistoryDto> getJobExecutionHistory(
            String jobName, LocalDate executionDate, String status) {

        List<JobExecution> allExecutions = new ArrayList<>();
        List<String> jobNames = StringUtils.hasText(jobName) ?
                jobExplorer.getJobNames().stream().filter(name -> name.toLowerCase().contains(jobName.toLowerCase())).collect(Collectors.toList()) :
                jobExplorer.getJobNames();

        for (String name : jobNames) {
            List<JobInstance> jobInstances = jobExplorer.getJobInstances(name, 0, MAX_INSTANCES_PER_JOB);
            for (JobInstance jobInstance : jobInstances) {
                allExecutions.addAll(jobExplorer.getJobExecutions(jobInstance));
            }
        }

        // 날짜 필터링 로직
        LocalDateTime startOfDay = (executionDate != null) ? executionDate.atStartOfDay() : null;
        LocalDateTime endOfDay = (executionDate != null) ? executionDate.atTime(LocalTime.MAX) : null;

        log.info("배치 기록 조회 조건 - 시작 시간: {}, 종료 시간: {}", startOfDay, endOfDay);

        return allExecutions.stream()
                .filter(exec -> startOfDay == null || (exec.getStartTime() != null && !exec.getStartTime().isBefore(startOfDay)))
                .filter(exec -> endOfDay == null || (exec.getStartTime() != null && !exec.getStartTime().isAfter(endOfDay)))
                .filter(exec -> !StringUtils.hasText(status) || exec.getStatus().name().equalsIgnoreCase(status))
                .sorted(Comparator.comparing(JobExecution::getStartTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(jobExecution -> new JobExecutionHistoryDto(
                        jobExecution.getId(),
                        jobExecution.getJobInstance().getJobName(),
                        jobExecution.getStartTime(),
                        jobExecution.getEndTime(),
                        jobExecution.getStatus().toString()
                ))
                .collect(Collectors.toList());
    }

    public JobExecutionDetailsDto getJobExecutionDetails(Long executionId) {
        JobExecution jobExecution = jobExplorer.getJobExecution(executionId);
        if (jobExecution == null) {
            return null;
        }

        StringBuilder stackTrace = new StringBuilder();
        if (jobExecution.getStatus().isUnsuccessful()) {
            for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
                if (stepExecution.getStatus().isUnsuccessful()) {
                    stackTrace.append("Step: ").append(stepExecution.getStepName()).append("\n");
                    stackTrace.append(stepExecution.getExitStatus().getExitDescription()).append("\n\n");
                }
            }
        }

        return new JobExecutionDetailsDto(
                jobExecution.getId(),
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStartTime(),
                jobExecution.getEndTime(),
                jobExecution.getStatus().toString(),
                stackTrace.toString(),
                jobExecution.getJobParameters().toString()
        );
    }
}
