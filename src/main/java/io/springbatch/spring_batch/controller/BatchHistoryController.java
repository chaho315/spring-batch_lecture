package io.springbatch.spring_batch.controller;

import io.springbatch.spring_batch.dto.JobExecutionDetailsDto;
import io.springbatch.spring_batch.dto.JobExecutionHistoryDto;
import io.springbatch.spring_batch.service.BatchHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BatchHistoryController {

    private final BatchHistoryService batchHistoryService;

    @GetMapping("/history")
    public String historyPage(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "history";
    }

    @GetMapping("/api/history/jobs")
    @ResponseBody
    public ResponseEntity<List<JobExecutionHistoryDto>> getJobExecutionHistory(
            @RequestParam(required = false) String jobName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate executionDate,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(batchHistoryService.getJobExecutionHistory(jobName, executionDate, status));
    }

    @GetMapping("/api/history/jobs/{executionId}")
    @ResponseBody
    public ResponseEntity<JobExecutionDetailsDto> getJobExecutionDetails(@PathVariable Long executionId) {
        JobExecutionDetailsDto details = batchHistoryService.getJobExecutionDetails(executionId);
        if (details == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(details);
    }
}
