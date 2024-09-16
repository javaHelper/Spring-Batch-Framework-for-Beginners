package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.decider.MyJobExecutionDecider;
import com.example.demo.listener.MyStepExecutionListener;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public MyStepExecutionListener myStepExecutionListener() {
		return new MyStepExecutionListener();
	}

	@Bean
	public MyJobExecutionDecider decider() {
		return new MyJobExecutionDecider();
	}

	@Bean
	public Step step1() {
		return this.stepBuilderFactory.get("step1")
				.tasklet(new Tasklet() {

					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("step1 executed!!");
						return RepeatStatus.FINISHED;
					}
				}).build();
	}

	@Bean
	public Step step2() {
		return this.stepBuilderFactory.get("step2")
				.tasklet(new Tasklet() {

					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

						boolean isFailed = false;

						if(isFailed) {
							throw new Exception("Test Exception");
						}

						System.out.println("step2 executed!!");
						return RepeatStatus.FINISHED;
					}
				}).build();
	}

	@Bean
	public Step step3() {
		return this.stepBuilderFactory.get("step3")
				.tasklet(new Tasklet() {

					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("step3 executed!!");
						return RepeatStatus.FINISHED;
					}
				}).build();
	}


	@Bean
	public Step step4() {
		return this.stepBuilderFactory.get("step4")
				.tasklet(new Tasklet() {

					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("step4 executed!!");
						return RepeatStatus.FINISHED;
					}
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
