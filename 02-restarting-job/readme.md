# Restarting Job

```curl
curl --location 'http://localhost:8080/launchJob/1'
```


SELECT * from test.BATCH_STEP_EXECUTION bse 


```
STEP_EXECUTION_ID|VERSION|STEP_NAME|JOB_EXECUTION_ID|START_TIME                   |END_TIME                     |STATUS   |COMMIT_COUNT|READ_COUNT|FILTER_COUNT|WRITE_COUNT|READ_SKIP_COUNT|WRITE_SKIP_COUNT|PROCESS_SKIP_COUNT|ROLLBACK_COUNT|EXIT_CODE|EXIT_MESSAGE                                                                                                                                                                                                                                                   |LAST_UPDATED                 |
-----------------+-------+---------+----------------+-----------------------------+-----------------------------+---------+------------+----------+------------+-----------+---------------+----------------+------------------+--------------+---------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+-----------------------------+
                1|      3|step1    |               1|2024-09-10 19:14:52.585000000|2024-09-10 19:14:52.601000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|                                                                                                                                                                                                                                                               |2024-09-10 19:14:52.602000000|
                2|      2|step2    |               1|2024-09-10 19:14:52.612000000|2024-09-10 19:14:52.622000000|FAILED   |           0|         0|           0|          0|              0|               0|                 0|             1|FAILED   |java.lang.Exception: Test Exception¶ at com.example.demo.config.BatchConfiguration$2.execute(BatchConfiguration.java:48)¶ at org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction(TaskletStep.java:407)¶ at org.spr|2024-09-10 19:14:52.622000000|
```

select * from test.BATCH_JOB_EXECUTION bje ;

```
JOB_EXECUTION_ID|VERSION|JOB_INSTANCE_ID|CREATE_TIME                  |START_TIME                   |END_TIME                     |STATUS|EXIT_CODE|EXIT_MESSAGE                                                                                                                                                                                                                                                   |LAST_UPDATED                 |JOB_CONFIGURATION_LOCATION|
----------------+-------+---------------+-----------------------------+-----------------------------+-----------------------------+------+---------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+-----------------------------+--------------------------+
               1|      2|              1|2024-09-10 19:14:52.527000000|2024-09-10 19:14:52.562000000|2024-09-10 19:14:52.627000000|FAILED|FAILED   |java.lang.Exception: Test Exception¶ at com.example.demo.config.BatchConfiguration$2.execute(BatchConfiguration.java:48)¶ at org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction(TaskletStep.java:407)¶ at org.spr|2024-09-10 19:14:52.627000000|                          |
```

Now change the boolean variable value to false;


SELECT * from test.BATCH_JOB_EXECUTION bje 

```
JOB_EXECUTION_ID|VERSION|JOB_INSTANCE_ID|CREATE_TIME                  |START_TIME                   |END_TIME                     |STATUS   |EXIT_CODE|EXIT_MESSAGE                                                                                                                                                                                                                                                   |LAST_UPDATED                 |JOB_CONFIGURATION_LOCATION|
----------------+-------+---------------+-----------------------------+-----------------------------+-----------------------------+---------+---------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+-----------------------------+--------------------------+
               1|      2|              1|2024-09-10 19:23:36.526000000|2024-09-10 19:23:36.558000000|2024-09-10 19:23:36.611000000|FAILED   |FAILED   |java.lang.Exception: Test Exception¶ at com.example.demo.config.BatchConfiguration$2.execute(BatchConfiguration.java:48)¶ at org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction(TaskletStep.java:407)¶ at org.spr|2024-09-10 19:23:36.611000000|                          |
               2|      2|              1|2024-09-10 19:23:51.188000000|2024-09-10 19:23:51.213000000|2024-09-10 19:23:51.267000000|COMPLETED|COMPLETED|                                                                                                                                                                                                                                                               |2024-09-10 19:23:51.267000000|                          |
```


SELECT * from test.BATCH_STEP_EXECUTION bse ;

```
STEP_EXECUTION_ID|VERSION|STEP_NAME|JOB_EXECUTION_ID|START_TIME                   |END_TIME                     |STATUS   |COMMIT_COUNT|READ_COUNT|FILTER_COUNT|WRITE_COUNT|READ_SKIP_COUNT|WRITE_SKIP_COUNT|PROCESS_SKIP_COUNT|ROLLBACK_COUNT|EXIT_CODE|EXIT_MESSAGE                                                                                                                                                                                                                                                   |LAST_UPDATED                 |
-----------------+-------+---------+----------------+-----------------------------+-----------------------------+---------+------------+----------+------------+-----------+---------------+----------------+------------------+--------------+---------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+-----------------------------+
                1|      3|step1    |               1|2024-09-10 19:23:36.573000000|2024-09-10 19:23:36.586000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|                                                                                                                                                                                                                                                               |2024-09-10 19:23:36.587000000|
                2|      2|step2    |               1|2024-09-10 19:23:36.598000000|2024-09-10 19:23:36.607000000|FAILED   |           0|         0|           0|          0|              0|               0|                 0|             1|FAILED   |java.lang.Exception: Test Exception¶ at com.example.demo.config.BatchConfiguration$2.execute(BatchConfiguration.java:48)¶ at org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction(TaskletStep.java:407)¶ at org.spr|2024-09-10 19:23:36.607000000|
                3|      3|step2    |               2|2024-09-10 19:23:51.232000000|2024-09-10 19:23:51.244000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|                                                                                                                                                                                                                                                               |2024-09-10 19:23:51.244000000|
                4|      3|step3    |               2|2024-09-10 19:23:51.254000000|2024-09-10 19:23:51.262000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|                                                                                                                                                                                                                                                               |2024-09-10 19:23:51.263000000|
```

```java
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

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
						boolean isSuccess = false;
						if(isSuccess) {
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
	public Job firstJob() {
		return this.jobBuilderFactory.get("job1")
				.start(step1())
				.next(step2())
				.next(step3())
				.build();
	}
}


```

