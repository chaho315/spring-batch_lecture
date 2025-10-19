package io.springbatch.spring_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ExcutionContextConfiguration {
   /* private final ExcutionContextTasklet1 excutionContextTasklet1;
    private final ExcutionContextTasklet2 excutionContextTasklet2;
    private final ExcutionContextTasklet3 excutionContextTasklet3;
    private final ExcutionContextTasklet4 excutionContextTasklet4;
    @Autowired
    private JobRepository jobRepository2;

    public ExcutionContextConfiguration(ExcutionContextTasklet1 excutionContextTasklet1, ExcutionContextTasklet2 excutionContextTasklet2, ExcutionContextTasklet3 excutionContextTasklet3, ExcutionContextTasklet4 excutionContextTasklet4) {
        this.excutionContextTasklet1 = excutionContextTasklet1;
        this.excutionContextTasklet2 = excutionContextTasklet2;
        this.excutionContextTasklet3 = excutionContextTasklet3;
        this.excutionContextTasklet4 = excutionContextTasklet4;
    }

    @Bean
    public Job excutionContextJob(JobRepository jobRepository, Step excutionContextstep1, Step excutionContextstep2, Step excutionContextstep3, Step excutionContextstep4) {
        System.out.println("======================================================");
        System.out.println("Job에서 사용하는 JobRepository: " + this.jobRepository2.toString());
        System.out.println("======================================================");
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
    }*/
}
