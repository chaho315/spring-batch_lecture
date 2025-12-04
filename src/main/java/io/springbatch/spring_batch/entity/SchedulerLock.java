package io.springbatch.spring_batch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SCHEDULER_LOCK")
@Getter
@Setter
public class SchedulerLock {

    @Id
    private String lockName;
}
