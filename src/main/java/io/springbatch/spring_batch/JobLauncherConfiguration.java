package io.springbatch.spring_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobLauncherConfiguration {
   /* @Bean
    public Job jobLauncherJob(JobRepository jobRepository, Step jobLauncherstep1, Step jobLauncherstep2, JobExecutionListener jobExecutionListener) {
        return new JobBuilder("jobLauncherJob", jobRepository)
                .start(jobLauncherstep1)
                .next(jobLauncherstep2)
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    public Step jobLauncherstep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("jobLauncherstep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    System.out.println("jobLauncherstep1 was excuted");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step jobLauncherstep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("jobLauncherstep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    System.out.println("jobLauncherstep2 was excuted");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }*/

}
