# conditional flow


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
					.on("COMPLETED").to(step2())
				.from(step2())
					.on("COMPLETED").to(step3())
				.from(step2())	
					.on("FAILED").to(step4())
				.end()
				.build();
	}
}
```


```curl
http://localhost:8080/launchJob/1
```



```logs
2024-09-11 16:42:26.071  INFO 66453 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2024-09-11 16:42:26.071  INFO 66453 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2024-09-11 16:42:26.072  INFO 66453 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2024-09-11 16:42:26.144  INFO 66453 --- [nio-8080-exec-1] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=job1]] launched with the following parameters: [{param=1}]
2024-09-11 16:42:26.176  INFO 66453 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step1]
step1 executed!!
2024-09-11 16:42:26.191  INFO 66453 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [step1] executed in 14ms
2024-09-11 16:42:26.202  INFO 66453 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step2]
step2 executed!!
2024-09-11 16:42:26.210  INFO 66453 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [step2] executed in 8ms
2024-09-11 16:42:26.219  INFO 66453 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step3]
step3 executed!!
2024-09-11 16:42:26.226  INFO 66453 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [step3] executed in 6ms
2024-09-11 16:42:26.232  INFO 66453 --- [nio-8080-exec-1] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=job1]] completed with the following parameters: [{param=1}] and the following status: [COMPLETED] in 76ms

```


SELECT * from test.BATCH_JOB_EXECUTION bje ;

```
JOB_EXECUTION_ID|VERSION|JOB_INSTANCE_ID|CREATE_TIME                  |START_TIME                   |END_TIME                     |STATUS   |EXIT_CODE|EXIT_MESSAGE|LAST_UPDATED                 |JOB_CONFIGURATION_LOCATION|
----------------+-------+---------------+-----------------------------+-----------------------------+-----------------------------+---------+---------+------------+-----------------------------+--------------------------+
               1|      2|              1|2024-09-11 16:42:26.119000000|2024-09-11 16:42:26.154000000|2024-09-11 16:42:26.230000000|COMPLETED|COMPLETED|            |2024-09-11 16:42:26.230000000|                          |
```


SELECT * from test.BATCH_STEP_EXECUTION bse ;

```
STEP_EXECUTION_ID|VERSION|STEP_NAME|JOB_EXECUTION_ID|START_TIME                   |END_TIME                     |STATUS   |COMMIT_COUNT|READ_COUNT|FILTER_COUNT|WRITE_COUNT|READ_SKIP_COUNT|WRITE_SKIP_COUNT|PROCESS_SKIP_COUNT|ROLLBACK_COUNT|EXIT_CODE|EXIT_MESSAGE|LAST_UPDATED                 |
-----------------+-------+---------+----------------+-----------------------------+-----------------------------+---------+------------+----------+------------+-----------+---------------+----------------+------------------+--------------+---------+------------+-----------------------------+
                1|      3|step1    |               1|2024-09-11 16:42:26.176000000|2024-09-11 16:42:26.190000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|            |2024-09-11 16:42:26.191000000|
                2|      3|step2    |               1|2024-09-11 16:42:26.202000000|2024-09-11 16:42:26.210000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|            |2024-09-11 16:42:26.210000000|
                3|      3|step3    |               1|2024-09-11 16:42:26.220000000|2024-09-11 16:42:26.226000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|            |2024-09-11 16:42:26.226000000|
```






```
2024-09-11 16:47:24.997  INFO 67313 --- [nio-8080-exec-2] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2024-09-11 16:47:24.997  INFO 67313 --- [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2024-09-11 16:47:24.998  INFO 67313 --- [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2024-09-11 16:47:25.064  INFO 67313 --- [nio-8080-exec-2] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=job1]] launched with the following parameters: [{param=2}]
2024-09-11 16:47:25.094  INFO 67313 --- [nio-8080-exec-2] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step1]
step1 executed!!
2024-09-11 16:47:25.108  INFO 67313 --- [nio-8080-exec-2] o.s.batch.core.step.AbstractStep         : Step: [step1] executed in 14ms
2024-09-11 16:47:25.124  INFO 67313 --- [nio-8080-exec-2] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step2]
2024-09-11 16:47:25.132 ERROR 67313 --- [nio-8080-exec-2] o.s.batch.core.step.AbstractStep         : Encountered an error executing step step2 in job job1

java.lang.Exception: Test Exception
	at com.example.demo.config.BatchConfiguration$2.execute(BatchConfiguration.java:50) ~[classes/:na]
	at org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction(TaskletStep.java:407) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction(TaskletStep.java:331) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.transaction.support.TransactionTemplate.execute(TransactionTemplate.java:140) ~[spring-tx-5.3.22.jar:5.3.22]
	at org.springframework.batch.core.step.tasklet.TaskletStep$2.doInChunkContext(TaskletStep.java:273) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.batch.core.scope.context.StepContextRepeatCallback.doInIteration(StepContextRepeatCallback.java:82) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.batch.repeat.support.RepeatTemplate.getNextResult(RepeatTemplate.java:375) ~[spring-batch-infrastructure-4.3.6.jar:4.3.6]
	at org.springframework.batch.repeat.support.RepeatTemplate.executeInternal(RepeatTemplate.java:215) ~[spring-batch-infrastructure-4.3.6.jar:4.3.6]
	at org.springframework.batch.repeat.support.RepeatTemplate.iterate(RepeatTemplate.java:145) ~[spring-batch-infrastructure-4.3.6.jar:4.3.6]
	at org.springframework.batch.core.step.tasklet.TaskletStep.doExecute(TaskletStep.java:258) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.batch.core.step.AbstractStep.execute(AbstractStep.java:208) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.batch.core.job.SimpleStepHandler.handleStep(SimpleStepHandler.java:152) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.batch.core.job.flow.JobFlowExecutor.executeStep(JobFlowExecutor.java:68) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.batch.core.job.flow.support.state.StepState.handle(StepState.java:68) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.batch.core.job.flow.support.SimpleFlow.resume(SimpleFlow.java:169) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.batch.core.job.flow.support.SimpleFlow.start(SimpleFlow.java:144) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.batch.core.job.flow.FlowJob.doExecute(FlowJob.java:137) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.batch.core.job.AbstractJob.execute(AbstractJob.java:320) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.batch.core.launch.support.SimpleJobLauncher$1.run(SimpleJobLauncher.java:149) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.core.task.SyncTaskExecutor.execute(SyncTaskExecutor.java:50) ~[spring-core-5.3.22.jar:5.3.22]
	at org.springframework.batch.core.launch.support.SimpleJobLauncher.run(SimpleJobLauncher.java:140) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
	at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:344) ~[spring-aop-5.3.22.jar:5.3.22]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:198) ~[spring-aop-5.3.22.jar:5.3.22]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163) ~[spring-aop-5.3.22.jar:5.3.22]
	at org.springframework.batch.core.configuration.annotation.SimpleBatchConfiguration$PassthruAdvice.invoke(SimpleBatchConfiguration.java:128) ~[spring-batch-core-4.3.6.jar:4.3.6]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186) ~[spring-aop-5.3.22.jar:5.3.22]
	at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:215) ~[spring-aop-5.3.22.jar:5.3.22]
	at com.sun.proxy.$Proxy59.run(Unknown Source) ~[na:na]
	at com.example.demo.controller.JobLaunchController.handle(JobLaunchController.java:27) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205) ~[spring-web-5.3.22.jar:5.3.22]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150) ~[spring-web-5.3.22.jar:5.3.22]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117) ~[spring-webmvc-5.3.22.jar:5.3.22]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895) ~[spring-webmvc-5.3.22.jar:5.3.22]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808) ~[spring-webmvc-5.3.22.jar:5.3.22]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-5.3.22.jar:5.3.22]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1070) ~[spring-webmvc-5.3.22.jar:5.3.22]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963) ~[spring-webmvc-5.3.22.jar:5.3.22]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006) ~[spring-webmvc-5.3.22.jar:5.3.22]
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898) ~[spring-webmvc-5.3.22.jar:5.3.22]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:655) ~[tomcat-embed-core-9.0.65.jar:4.0.FR]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883) ~[spring-webmvc-5.3.22.jar:5.3.22]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:764) ~[tomcat-embed-core-9.0.65.jar:4.0.FR]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:227) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-9.0.65.jar:9.0.65]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-5.3.22.jar:5.3.22]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117) ~[spring-web-5.3.22.jar:5.3.22]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-5.3.22.jar:5.3.22]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117) ~[spring-web-5.3.22.jar:5.3.22]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-5.3.22.jar:5.3.22]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117) ~[spring-web-5.3.22.jar:5.3.22]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:197) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:541) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:135) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:360) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:399) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:890) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1789) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-9.0.65.jar:9.0.65]
	at java.base/java.lang.Thread.run(Thread.java:834) ~[na:na]

2024-09-11 16:47:25.133  INFO 67313 --- [nio-8080-exec-2] o.s.batch.core.step.AbstractStep         : Step: [step2] executed in 9ms
2024-09-11 16:47:25.149  INFO 67313 --- [nio-8080-exec-2] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step4]
step4 executed!!
2024-09-11 16:47:25.156  INFO 67313 --- [nio-8080-exec-2] o.s.batch.core.step.AbstractStep         : Step: [step4] executed in 7ms
2024-09-11 16:47:25.161  INFO 67313 --- [nio-8080-exec-2] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=job1]] completed with the following parameters: [{param=2}] and the following status: [COMPLETED] in 84ms


```


SELECT * from test.BATCH_JOB_EXECUTION bje ;

```
JOB_EXECUTION_ID|VERSION|JOB_INSTANCE_ID|CREATE_TIME                  |START_TIME                   |END_TIME                     |STATUS   |EXIT_CODE|EXIT_MESSAGE|LAST_UPDATED                 |JOB_CONFIGURATION_LOCATION|
----------------+-------+---------------+-----------------------------+-----------------------------+-----------------------------+---------+---------+------------+-----------------------------+--------------------------+
               1|      2|              1|2024-09-11 16:42:26.119000000|2024-09-11 16:42:26.154000000|2024-09-11 16:42:26.230000000|COMPLETED|COMPLETED|            |2024-09-11 16:42:26.230000000|                          |
               2|      2|              2|2024-09-11 16:47:25.046000000|2024-09-11 16:47:25.075000000|2024-09-11 16:47:25.159000000|COMPLETED|COMPLETED|            |2024-09-11 16:47:25.159000000|                          |
```


SELECT * from test.BATCH_STEP_EXECUTION bse ;


```
STEP_EXECUTION_ID|VERSION|STEP_NAME|JOB_EXECUTION_ID|START_TIME                   |END_TIME                     |STATUS   |COMMIT_COUNT|READ_COUNT|FILTER_COUNT|WRITE_COUNT|READ_SKIP_COUNT|WRITE_SKIP_COUNT|PROCESS_SKIP_COUNT|ROLLBACK_COUNT|EXIT_CODE|EXIT_MESSAGE                                                                                                                                                                                                                                                   |LAST_UPDATED                 |
-----------------+-------+---------+----------------+-----------------------------+-----------------------------+---------+------------+----------+------------+-----------+---------------+----------------+------------------+--------------+---------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+-----------------------------+
                1|      3|step1    |               1|2024-09-11 16:42:26.176000000|2024-09-11 16:42:26.190000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|                                                                                                                                                                                                                                                               |2024-09-11 16:42:26.191000000|
                2|      3|step2    |               1|2024-09-11 16:42:26.202000000|2024-09-11 16:42:26.210000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|                                                                                                                                                                                                                                                               |2024-09-11 16:42:26.210000000|
                3|      3|step3    |               1|2024-09-11 16:42:26.220000000|2024-09-11 16:42:26.226000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|                                                                                                                                                                                                                                                               |2024-09-11 16:42:26.226000000|
                4|      3|step1    |               2|2024-09-11 16:47:25.094000000|2024-09-11 16:47:25.108000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|                                                                                                                                                                                                                                                               |2024-09-11 16:47:25.108000000|
                5|      3|step2    |               2|2024-09-11 16:47:25.124000000|2024-09-11 16:47:25.133000000|ABANDONED|           0|         0|           0|          0|              0|               0|                 0|             1|FAILED   |java.lang.Exception: Test Exception¶ at com.example.demo.config.BatchConfiguration$2.execute(BatchConfiguration.java:50)¶ at org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction(TaskletStep.java:407)¶ at org.spr|2024-09-11 16:47:25.138000000|
                6|      3|step4    |               2|2024-09-11 16:47:25.149000000|2024-09-11 16:47:25.156000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|                                                                                                                                                                                                                                                               |2024-09-11 16:47:25.156000000|
```
