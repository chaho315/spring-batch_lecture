package io.springbatch.spring_batch.repository;

import io.springbatch.spring_batch.entity.SchedulerLock;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface SchedulerLockRepository extends JpaRepository<SchedulerLock, String> {

    /**
     * 'batch_job_lock'이라는 이름의 행에 대해 비관적 쓰기 락(SELECT FOR UPDATE)을 시도합니다.
     * jakarta.persistence.lock.timeout 힌트를 0으로 설정하여, 락을 즉시 획득할 수 없으면 대기하지 않고 바로 예외를 발생시킵니다.
     * 이를 통해 여러 인스턴스 중 하나만 락을 획득하고 나머지는 빠르게 실패하도록 합니다.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")})
    @Query("SELECT l FROM SchedulerLock l WHERE l.lockName = :lockName")
    Optional<SchedulerLock> findAndLock(String lockName);
}
