package io.springbatch.spring_batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
public class DBJobConfiguration {

    /*@Bean
    public Job job(JobRepository jobRepository, Step Step1, Step Step2) {
        return new JobBuilder("job", jobRepository)
                .start(Step1)
                .next(Step2)
                .build();
    }

    @Bean
    public Step Step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Step1",jobRepository)
                *//*.tasklet((contribution, chunkContext) -> {
                   //contribution을 이용한 방법
                    JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
                    jobParameters.getString("name");
                    jobParameters.getLong("seq");
                    jobParameters.getDate("date");
                    jobParameters.getDouble("age");

                    //chunkContext를 이용한 방법
                    //chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters(); 1번방식
                    Map<String, Object> jobParameters1 = chunkContext.getStepContext().getJobParameters();//2번방식
                    System.out.println("Step1 was excuted");
                    return RepeatStatus.FINISHED;
                },transactionManager)*//*
                .tasklet(new CustomTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step Step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Step2",jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Step2 was excuted");

                    return RepeatStatus.FINISHED;
                },transactionManager)
                .build();
    }*/
}
