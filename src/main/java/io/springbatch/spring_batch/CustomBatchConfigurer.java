package io.springbatch.spring_batch;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class CustomBatchConfigurer{
    @Bean
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();

        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);

        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        factory.setTablePrefix("SYSTEM_");
        factory.setMaxVarCharLength(1000);

        factory.afterPropertiesSet(); // 설정 값 검증
        return factory.getObject(); // JobRepository 생성
    }

    //비동기 방식 설정
    /*@Bean
    @Qualifier("customAsyncJobLauncher")
    public JobLauncher asyncJobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor()); // 👈 여기서 설정
        return jobLauncher;
    }*/

    //동기 방식 설정
    /*@Bean
    public JobLauncher syncJobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();

        // 1. JobRepository는 필수이므로 설정합니다.
        jobLauncher.setJobRepository(jobRepository);

        // 2. setTaskExecutor()를 호출하지 않습니다.
        //    이렇게 하면 JobLauncher가 동기(synchronous) 방식으로 동작합니다.

        return jobLauncher;
    }*/
}
