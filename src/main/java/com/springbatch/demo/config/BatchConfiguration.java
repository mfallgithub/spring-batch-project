package com.springbatch.demo.config;

import com.springbatch.demo.decider.MyJobExecutionDecider;
import com.springbatch.demo.listener.MyStepExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public StepExecutionListener myStepExecutionListener(){
        return new MyStepExecutionListener() ;
    }

    @Bean
    public JobExecutionDecider decider(){
        return new MyJobExecutionDecider();
    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("Step 1 executed!!");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step step2() {
        return this.stepBuilderFactory.get("step2").tasklet((stepContribution, chunkContext) -> {
            boolean isFaillure = false;
            if (isFaillure) {
                throw new Exception("Test Exception");
            }
            System.out.println("step 2 executed!!");
            return RepeatStatus.FINISHED;
        })
                //.listener(myStepExecutionListener())
                .build();
    }

    @Bean
    public Step step3() {
        return this.stepBuilderFactory.get("step3").tasklet((stepContribution, chunkContext) -> {
            System.out.println("Step 3 executed!!");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step step4() {
        return this.stepBuilderFactory.get("step4").tasklet((stepContribution, chunkContext) -> {
            System.out.println("Step 4 executed!!");
            return RepeatStatus.FINISHED;
        }).build();
    }


    @Bean
    public Job firstJob() {
        return this.jobBuilderFactory.get("job1")
                .start(step1())
                    .on("COMPLETED").to(decider())
                         .on("TEST_STATUS").to(step2())
                .from(decider())
                          .on("*").to(step3())
                .end()
                .build();
    }

}
