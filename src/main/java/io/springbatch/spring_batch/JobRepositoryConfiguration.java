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
public class JobRepositoryConfiguration {
    /*@Bean
    public Job repositoryJob(JobRepository jobRepository, Step repositorystep1, Step repositorystep2, JobExecutionListener jobExecutionListener) {
        return new JobBuilder("repositoryJob", jobRepository)
                .start(repositorystep1)
                .next(repositorystep2)
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    public Step repositorystep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("repositorystep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    System.out.println("repositorystep1 was excuted");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step repositorystep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("repositorystep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    System.out.println("repositorystep2 was excuted");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }*/

}
