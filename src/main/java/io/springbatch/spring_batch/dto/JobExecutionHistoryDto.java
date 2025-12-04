package io.springbatch.spring_batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobExecutionHistoryDto {
    private Long id;
    private String jobName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
