package io.springbatch.spring_batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobStatus {
    private String jobName;
    private String cronExpression;
}
