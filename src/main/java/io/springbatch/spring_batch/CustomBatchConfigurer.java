package io.springbatch.spring_batch;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
}
