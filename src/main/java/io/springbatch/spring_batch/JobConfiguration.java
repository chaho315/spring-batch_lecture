package io.springbatch.spring_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfiguration {
    /*@Bean
    public Job batchJob(JobRepository jobRepository, Step Step1, Step Step2) {
        return new JobBuilder("batchJob", jobRepository)
                .start(Step1)
                .next(Step2)
                .build();
    }*/

    @Bean
    public Job batchJob2(JobRepository jobRepository, Flow flow, Step step5) {
        return new JobBuilder("batchJob2", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(flow)
                .next(step5)
                .end()
                .build();
    }

    @Bean
    public Step Step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("batchJob step1 has been executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step Step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Step2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("batchJob step2 has been executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Flow flow(Step step3, Step step4) {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
        flowBuilder.start(step3)
                .next(step4)
                .end();
        return flowBuilder.build();
    }

    @Bean
    public Step step3(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step3", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("batchJob step3 has been executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
    @Bean
    public Step step4(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step4", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("batchJob step4 has been executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step5(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step5", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("batchJob step5 has been executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
