package io.springbatch.spring_batch.job;

import io.springbatch.spring_batch.service.DistributedLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchJob extends QuartzJobBean {

    private final JobLauncher jobLauncher;
    private final ApplicationContext applicationContext;
    private final DistributedLockService distributedLockService;
    private final JobExplorer jobExplorer; // JobExplorer 주입

    @Override
    protected void executeInternal(JobExecutionContext context) {
        // 분산 락 및 로컬 락 획득 시도
        if (distributedLockService.tryLock()) {
            try {
                String jobName = context.getJobDetail().getKey().getName();
                log.info(">>>>>> {} 배치 Job 실행 시작", jobName);

                Job job = (Job) applicationContext.getBean(jobName);

                // JobParametersIncrementer를 사용하여 다음 JobParameters를 가져옴
                JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                        .getNextJobParameters(job)
                        .toJobParameters();

                jobLauncher.run(job, jobParameters);

                log.info("<<<<<< {} 배치 Job 실행 완료", jobName);

            } catch (Exception e) {
                log.error("배치 Job 실행 중 오류 발생", e);
            }
        }
        // 락 획득에 실패하면 아무것도 하지 않고 조용히 종료
    }
}
