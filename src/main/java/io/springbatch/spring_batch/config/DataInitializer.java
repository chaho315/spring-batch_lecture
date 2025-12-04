package io.springbatch.spring_batch.config;

import io.springbatch.spring_batch.entity.SchedulerLock;
import io.springbatch.spring_batch.repository.SchedulerLockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final SchedulerLockRepository schedulerLockRepository;
    private static final String LOCK_NAME = "batch_job_lock";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 애플리케이션 시작 시 'batch_job_lock' 데이터가 없으면 생성
        if (!schedulerLockRepository.existsById(LOCK_NAME)) {
            log.info("분산 락을 위한 초기 데이터 '{}'를 생성합니다.", LOCK_NAME);
            SchedulerLock lock = new SchedulerLock();
            lock.setLockName(LOCK_NAME);
            schedulerLockRepository.save(lock);
        } else {
            log.info("분산 락을 위한 초기 데이터 '{}'가 이미 존재합니다.", LOCK_NAME);
        }
    }
}
