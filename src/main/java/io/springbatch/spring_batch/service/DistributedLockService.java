package io.springbatch.spring_batch.service;

import io.springbatch.spring_batch.repository.SchedulerLockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributedLockService {

    private final SchedulerLockRepository schedulerLockRepository;
    private static final String LOCK_NAME = "batch_job_lock";
    private static final long LOCAL_LOCK_DURATION_SECONDS = 10; // 1분에서 10초로 변경

    // 로컬 락을 위한 마지막 실행 시간 기록 (스레드 안전성을 위해 AtomicReference 사용)
    private final AtomicReference<LocalDateTime> lastExecutionTime = new AtomicReference<>();

    /**
     * 분산 락과 로컬 락을 모두 획득하려고 시도합니다.
     * @return 락 획득에 성공하면 true, 실패하면 false
     */
    @Transactional
    public boolean tryLock() {
        // 1. 로컬 락 확인
        if (isLocalLockActive()) {
            log.info("로컬 락이 활성화되어 있어 스케줄을 건너뜁니다. 마지막 실행: {}", lastExecutionTime.get());
            return false;
        }

        // 2. 분산 락 시도
        try {
            log.info("분산 락 획득 시도...");
            schedulerLockRepository.findAndLock(LOCK_NAME);
            log.info("분산 락 획득 성공!");

            // 3. 락 성공 시 로컬 락(타임스탬프) 설정
            lastExecutionTime.set(LocalDateTime.now());
            return true;

        } catch (Exception e) {
            // 락을 획득하지 못한 경우 (PessimisticLockingFailureException 등)
            log.info("다른 인스턴스가 이미 락을 획득하여 스케줄을 건너뜁니다.");
            return false;
        }
    }

    private boolean isLocalLockActive() {
        LocalDateTime lastTime = lastExecutionTime.get();
        if (lastTime == null) {
            return false; // 첫 실행
        }
        // 시간 단위를 분(MINUTES)에서 초(SECONDS)로 변경
        long secondsSinceLastRun = ChronoUnit.SECONDS.between(lastTime, LocalDateTime.now());
        return secondsSinceLastRun < LOCAL_LOCK_DURATION_SECONDS;
    }
}
