# Sequential Job

```curl
curl --location 'http://localhost:8080/launchJob/1'
```


```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.1)

2024-09-10 19:04:52.816  INFO 39431 --- [           main] c.e.demo.SequentialFlowApplication       : Starting SequentialFlowApplication using Java 17.0.1 on Prateeks-MacBook-Pro.local with PID 39431 (/Users/prats/Documents/Prateek/Spring-Batch-Framework-for-Beginners/01-sequential-flow/target/classes started by prateekashtikar in /Users/prats/Documents/Prateek/Spring-Batch-Framework-for-Beginners/01-sequential-flow)
2024-09-10 19:04:52.820  INFO 39431 --- [           main] c.e.demo.SequentialFlowApplication       : No active profile set, falling back to 1 default profile: "default"
2024-09-10 19:04:54.069  INFO 39431 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2024-09-10 19:04:54.077  INFO 39431 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-09-10 19:04:54.077  INFO 39431 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.64]
2024-09-10 19:04:54.169  INFO 39431 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-09-10 19:04:54.169  INFO 39431 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1288 ms
2024-09-10 19:04:54.343  INFO 39431 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2024-09-10 19:04:54.774  INFO 39431 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2024-09-10 19:04:55.287  INFO 39431 --- [           main] o.s.b.c.r.s.JobRepositoryFactoryBean     : No database type set, using meta data indicating: MYSQL
2024-09-10 19:04:55.301  INFO 39431 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : No TaskExecutor has been set, defaulting to synchronous executor.
2024-09-10 19:04:55.425  INFO 39431 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2024-09-10 19:04:55.436  INFO 39431 --- [           main] c.e.demo.SequentialFlowApplication       : Started SequentialFlowApplication in 3.137 seconds (JVM running for 3.979)
2024-09-10 19:04:57.336  INFO 39431 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2024-09-10 19:04:57.336  INFO 39431 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2024-09-10 19:04:57.338  INFO 39431 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2024-09-10 19:04:57.505  INFO 39431 --- [nio-8080-exec-1] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=job1]] launched with the following parameters: [{param=1}]
2024-09-10 19:04:57.547  INFO 39431 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step2]
step2 executed!!
2024-09-10 19:04:57.578  INFO 39431 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [step2] executed in 31ms
2024-09-10 19:04:57.606  INFO 39431 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step1]
step1 executed!!
2024-09-10 19:04:57.618  INFO 39431 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [step1] executed in 11ms
2024-09-10 19:04:57.641  INFO 39431 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step3]
step3 executed!!
2024-09-10 19:04:57.660  INFO 39431 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [step3] executed in 19ms
2024-09-10 19:04:57.678  INFO 39431 --- [nio-8080-exec-1] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=job1]] completed with the following parameters: [{param=1}] and the following status: [COMPLETED] in 150ms

```



```
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
				.start(step2())
				.next(step1())
				.next(step3())
				.build();
	}
}

```
