package io.springbatch.spring_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JobRunner implements ApplicationRunner {
   private final JobLauncher jobLauncher;
   private final Job job;
   private final Job excutionContextJob;

    public JobRunner(JobLauncher jobLauncher, Job job, Job excutionContextJob) {
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.excutionContextJob = excutionContextJob;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name","user1")
                .addDate("runDate",new Date())
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
        jobLauncher.run(excutionContextJob, jobParameters);
    }
}
