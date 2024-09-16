#

SELECT * from test.BATCH_JOB_EXECUTION bje ;

```
JOB_EXECUTION_ID|VERSION|JOB_INSTANCE_ID|CREATE_TIME                  |START_TIME                   |END_TIME                     |STATUS   |EXIT_CODE|EXIT_MESSAGE|LAST_UPDATED                 |
----------------+-------+---------------+-----------------------------+-----------------------------+-----------------------------+---------+---------+------------+-----------------------------+
               1|      2|              1|2024-09-12 17:37:21.717122000|2024-09-12 17:37:21.754708000|2024-09-12 17:37:21.999805000|COMPLETED|COMPLETED|            |2024-09-12 17:37:21.999976000|
               2|      2|              2|2024-09-12 17:37:21.888779000|2024-09-12 17:37:21.897598000|2024-09-12 17:37:21.982098000|COMPLETED|COMPLETED|            |2024-09-12 17:37:21.982452000|
```


SELECT * from test.BATCH_STEP_EXECUTION bse ;

```
STEP_EXECUTION_ID|VERSION|STEP_NAME|JOB_EXECUTION_ID|CREATE_TIME                  |START_TIME                   |END_TIME                     |STATUS   |COMMIT_COUNT|READ_COUNT|FILTER_COUNT|WRITE_COUNT|READ_SKIP_COUNT|WRITE_SKIP_COUNT|PROCESS_SKIP_COUNT|ROLLBACK_COUNT|EXIT_CODE|EXIT_MESSAGE|LAST_UPDATED                 |
-----------------+-------+---------+----------------+-----------------------------+-----------------------------+-----------------------------+---------+------------+----------+------------+-----------+---------------+----------------+------------------+--------------+---------+------------+-----------------------------+
                1|      3|step3    |               1|2024-09-12 17:37:21.774525000|2024-09-12 17:37:21.788981000|2024-09-12 17:37:21.812519000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|            |2024-09-12 17:37:21.813482000|
                2|      3|step4    |               1|2024-09-12 17:37:21.824503000|2024-09-12 17:37:21.831778000|2024-09-12 17:37:21.846168000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|            |2024-09-12 17:37:21.846596000|
                3|      2|job3Step |               1|2024-09-12 17:37:21.862114000|2024-09-12 17:37:21.870610000|2024-09-12 17:37:21.995529000|COMPLETED|           0|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|            |2024-09-12 17:37:21.995972000|
                4|      3|step5    |               2|2024-09-12 17:37:21.909165000|2024-09-12 17:37:21.914266000|2024-09-12 17:37:21.924771000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|            |2024-09-12 17:37:21.925751000|
                5|      3|step6    |               2|2024-09-12 17:37:21.935408000|2024-09-12 17:37:21.943070000|2024-09-12 17:37:21.970969000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|            |2024-09-12 17:37:21.971529000|
```



```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.3.3)

2024-09-12T17:37:16.517+05:30  INFO 26167 --- [           main] com.springbatch.demo.DemoApplication     : Starting DemoApplication using Java 17.0.1 with PID 26167 (/Users/prats/Documents/Prateek/Spring-Batch-Framework-for-Beginners/18-nested-jobs/target/classes started by prateekashtikar in /Users/prats/Documents/Prateek/Spring-Batch-Framework-for-Beginners/18-nested-jobs)
2024-09-12T17:37:16.524+05:30  INFO 26167 --- [           main] com.springbatch.demo.DemoApplication     : No active profile set, falling back to 1 default profile: "default"
2024-09-12T17:37:17.649+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration$Hikari' of type [org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration$Hikari] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [jobRegistryBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
2024-09-12T17:37:17.701+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties' of type [org.springframework.boot.autoconfigure.jdbc.DataSourceProperties] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [jobRegistryBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
2024-09-12T17:37:17.703+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration$PooledDataSourceConfiguration' of type [org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration$PooledDataSourceConfiguration] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [jobRegistryBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
2024-09-12T17:37:17.704+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'jdbcConnectionDetails' of type [org.springframework.boot.autoconfigure.jdbc.PropertiesJdbcConnectionDetails] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [jobRegistryBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
2024-09-12T17:37:17.739+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'dataSource' of type [com.zaxxer.hikari.HikariDataSource] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [jobRegistryBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
2024-09-12T17:37:17.754+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration$JdbcTransactionManagerConfiguration' of type [org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration$JdbcTransactionManagerConfiguration] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [jobRegistryBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
2024-09-12T17:37:17.760+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizationAutoConfiguration' of type [org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizationAutoConfiguration] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [jobRegistryBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
2024-09-12T17:37:17.776+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'transactionExecutionListeners' of type [org.springframework.boot.autoconfigure.transaction.ExecutionListenersTransactionManagerCustomizer] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [jobRegistryBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
2024-09-12T17:37:17.787+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'spring.transaction-org.springframework.boot.autoconfigure.transaction.TransactionProperties' of type [org.springframework.boot.autoconfigure.transaction.TransactionProperties] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [jobRegistryBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
2024-09-12T17:37:17.789+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'platformTransactionManagerCustomizers' of type [org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [jobRegistryBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
2024-09-12T17:37:17.794+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'transactionManager' of type [org.springframework.jdbc.support.JdbcTransactionManager] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [jobRegistryBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
2024-09-12T17:37:17.802+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'spring.batch-org.springframework.boot.autoconfigure.batch.BatchProperties' of type [org.springframework.boot.autoconfigure.batch.BatchProperties] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). Is this bean getting eagerly injected into a currently created BeanPostProcessor [jobRegistryBeanPostProcessor]? Check the corresponding BeanPostProcessor declaration and its dependencies.
2024-09-12T17:37:17.814+05:30  WARN 26167 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration$SpringBootBatchConfiguration' of type [org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration$SpringBootBatchConfiguration] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying). The currently created BeanPostProcessor [jobRegistryBeanPostProcessor] is declared through a non-static factory method on that class; consider declaring it as static instead.
2024-09-12T17:37:17.994+05:30  INFO 26167 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2024-09-12T17:37:18.006+05:30  INFO 26167 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-09-12T17:37:18.007+05:30  INFO 26167 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.28]
2024-09-12T17:37:18.059+05:30  INFO 26167 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-09-12T17:37:18.060+05:30  INFO 26167 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1487 ms
2024-09-12T17:37:18.222+05:30  INFO 26167 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2024-09-12T17:37:18.601+05:30  INFO 26167 --- [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection com.mysql.cj.jdbc.ConnectionImpl@100aa331
2024-09-12T17:37:18.603+05:30  INFO 26167 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2024-09-12T17:37:18.769+05:30  INFO 26167 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : No TaskExecutor has been set, defaulting to synchronous executor.
2024-09-12T17:37:19.294+05:30  INFO 26167 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2024-09-12T17:37:19.307+05:30  INFO 26167 --- [           main] com.springbatch.demo.DemoApplication     : Started DemoApplication in 3.362 seconds (process running for 4.188)
This is a Test
2024-09-12T17:37:21.567+05:30  INFO 26167 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2024-09-12T17:37:21.568+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2024-09-12T17:37:21.570+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 2 ms
2024-09-12T17:37:21.745+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=job2]] launched with the following parameters: [{'param':'{value=1, type=class java.lang.String, identifying=true}'}]
2024-09-12T17:37:21.788+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step3]
step3 executed!!
2024-09-12T17:37:21.813+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [step3] executed in 23ms
2024-09-12T17:37:21.829+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step4]
step4 executed!!
2024-09-12T17:37:21.846+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [step4] executed in 14ms
2024-09-12T17:37:21.870+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [job3Step]
2024-09-12T17:37:21.897+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=job3]] launched with the following parameters: [{'param':'{value=1, type=class java.lang.String, identifying=true}'}]
2024-09-12T17:37:21.914+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step5]
step5 executed!!
2024-09-12T17:37:21.924+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [step5] executed in 10ms
2024-09-12T17:37:21.942+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step6]
step6 executed!!
2024-09-12T17:37:21.971+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [step6] executed in 27ms
2024-09-12T17:37:21.987+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=job3]] completed with the following parameters: [{'param':'{value=1, type=class java.lang.String, identifying=true}'}] and the following status: [COMPLETED] in 84ms
2024-09-12T17:37:21.995+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [job3Step] executed in 124ms
2024-09-12T17:37:22.004+05:30  INFO 26167 --- [nio-8080-exec-1] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=job2]] completed with the following parameters: [{'param':'{value=1, type=class java.lang.String, identifying=true}'}] and the following status: [COMPLETED] in 245ms
2024-09-12T18:06:41.744+05:30  WARN 26167 --- [l-1 housekeeper] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Thread starvation or clock leap detected (housekeeper delta=28m53s29ms).

```