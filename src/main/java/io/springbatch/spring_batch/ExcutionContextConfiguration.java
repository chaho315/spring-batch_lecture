package io.springbatch.spring_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ExcutionContextConfiguration {
    private ExcutionContextTasklet1 excutionContextTasklet1;
    private ExcutionContextTasklet2 excutionContextTasklet2;
    private ExcutionContextTasklet3 excutionContextTasklet3;
    private ExcutionContextTasklet4 excutionContextTasklet4;

    @Bean
    public Job excutionContextJob(JobRepository jobRepository, Step excutionContextstep1, Step excutionContextstep2, Step excutionContextstep3, Step excutionContextstep4) {
        return new JobBuilder("excutionContextJob", jobRepository)
                .start(excutionContextstep1)
                .next(excutionContextstep2)
                .next(excutionContextstep3)
                .next(excutionContextstep4)
                .build();
    }

    @Bean
    public Step excutionContextstep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("excutionContextstep1", jobRepository)
                .tasklet(excutionContextTasklet1, transactionManager)
                .build();
    }

    @Bean
    public Step excutionContextstep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("excutionContextstep2", jobRepository)
                .tasklet(excutionContextTasklet2, transactionManager)
                .build();
    }

    @Bean
    public Step excutionContextstep3(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("excutionContextstep3", jobRepository)
                .tasklet(excutionContextTasklet3, transactionManager)
                .build();
    }

    @Bean
    public Step excutionContextstep4(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("excutionContextstep4", jobRepository)
                .tasklet(excutionContextTasklet4, transactionManager)
                .build();
    }
}
