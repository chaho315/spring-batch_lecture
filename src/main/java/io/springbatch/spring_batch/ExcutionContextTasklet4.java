package io.springbatch.spring_batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ExcutionContextTasklet4 implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("name : "+chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("name") );
        System.out.println("ExcutionContextTasklet4");
        return RepeatStatus.FINISHED;
    }
}
