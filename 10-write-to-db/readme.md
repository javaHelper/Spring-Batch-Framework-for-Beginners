#


```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.3)

2024-09-12 01:49:41.877  INFO 4077 --- [           main] c.s.c.ChunkProcessingApplication         : Starting ChunkProcessingApplication using Java 17.0.1 on Prateeks-MacBook-Pro.local with PID 4077 (/Users/prats/Documents/Prateek/Spring-Batch-Framework-for-Beginners/10-write-to-db/target/classes started by prateekashtikar in /Users/prats/Documents/Prateek/Spring-Batch-Framework-for-Beginners/10-write-to-db)
2024-09-12 01:49:41.883  INFO 4077 --- [           main] c.s.c.ChunkProcessingApplication         : No active profile set, falling back to 1 default profile: "default"
2024-09-12 01:49:43.160  INFO 4077 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2024-09-12 01:49:43.168  INFO 4077 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-09-12 01:49:43.169  INFO 4077 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.65]
2024-09-12 01:49:43.264  INFO 4077 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-09-12 01:49:43.264  INFO 4077 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1318 ms
2024-09-12 01:49:43.402  INFO 4077 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2024-09-12 01:49:43.823  INFO 4077 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2024-09-12 01:49:44.424  INFO 4077 --- [           main] o.s.b.c.r.s.JobRepositoryFactoryBean     : No database type set, using meta data indicating: MYSQL
2024-09-12 01:49:44.436  INFO 4077 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : No TaskExecutor has been set, defaulting to synchronous executor.
2024-09-12 01:49:44.561  INFO 4077 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2024-09-12 01:49:44.574  INFO 4077 --- [           main] c.s.c.ChunkProcessingApplication         : Started ChunkProcessingApplication in 3.217 seconds (JVM running for 4.084)
2024-09-12 01:49:47.863  INFO 4077 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2024-09-12 01:49:47.864  INFO 4077 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2024-09-12 01:49:47.865  INFO 4077 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2024-09-12 01:49:48.037  INFO 4077 --- [nio-8080-exec-1] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=job1]] launched with the following parameters: [{param=1}]
2024-09-12 01:49:48.078  INFO 4077 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [chunkBasedStep1]
ResultSet object = HikariProxyResultSet@838916103 wrapping com.mysql.cj.jdbc.result.ResultSetImpl@2aceefd5
ResultSet object = HikariProxyResultSet@838916103 wrapping com.mysql.cj.jdbc.result.ResultSetImpl@2aceefd5
ResultSet object = HikariProxyResultSet@838916103 wrapping com.mysql.cj.jdbc.result.ResultSetImpl@2aceefd5
ResultSet object = HikariProxyResultSet@431330516 wrapping com.mysql.cj.jdbc.result.ResultSetImpl@587e6343
ResultSet object = HikariProxyResultSet@431330516 wrapping com.mysql.cj.jdbc.result.ResultSetImpl@587e6343
2024-09-12 01:49:48.133  INFO 4077 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [chunkBasedStep1] executed in 54ms
2024-09-12 01:49:48.143  INFO 4077 --- [nio-8080-exec-1] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=job1]] completed with the following parameters: [{param=1}] and the following status: [COMPLETED] in 87ms

```



```java

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;
	
	
	@Bean
	public ItemReader<Product> jdbcPagingItemReader() throws Exception {
		JdbcPagingItemReader<Product> itemReader = new JdbcPagingItemReader<>();
		itemReader.setDataSource(dataSource);
		
		SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
		factory.setDataSource(dataSource);
		factory.setSelectClause("select product_id, product_name, product_category, product_price");
		factory.setFromClause("from product_details");
		factory.setSortKey("product_id");
		
		itemReader.setQueryProvider(factory.getObject());
		itemReader.setRowMapper(new ProductRowMapper());
		itemReader.setPageSize(3);
		
		return itemReader;
	}
	
	
	@Bean
	public JdbcBatchItemWriter<Product> jdbcBatchItemWriter() {
		JdbcBatchItemWriter<Product> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setSql("insert into product_details_output values (?,?,?,?)");
		itemWriter.setItemPreparedStatementSetter(new ProductItemPreparedStatementSetter());
		return itemWriter;
	}
	
	@Bean
	public Step step1() throws Exception {
		return this.stepBuilderFactory.get("chunkBasedStep1")
				.<Product,Product>chunk(3)
				.reader(jdbcPagingItemReader())
				.writer(jdbcBatchItemWriter())
				.build();
	}
	
	@Bean
	public Job firstJob() throws Exception {
		return this.jobBuilderFactory.get("job1")
				.start(step1())
				.build();
	}
}

```