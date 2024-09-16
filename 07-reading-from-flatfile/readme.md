# Read from CSV


```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.3)

2024-09-11 19:29:05.159  INFO 87094 --- [           main] c.s.c.ChunkProcessingApplication         : Starting ChunkProcessingApplication using Java 11.0.22 on Prateeks-MacBook-Pro.local with PID 87094 (/Users/prats/Documents/Prateek/Spring-Batch-Framework-for-Beginners/07-reading-from-flatfile/target/classes started by prateekashtikar in /Users/prats/Documents/Prateek/Spring-Batch-Framework-for-Beginners/07-reading-from-flatfile)
2024-09-11 19:29:05.160  INFO 87094 --- [           main] c.s.c.ChunkProcessingApplication         : No active profile set, falling back to 1 default profile: "default"
2024-09-11 19:29:05.634  INFO 87094 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2024-09-11 19:29:05.640  INFO 87094 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-09-11 19:29:05.640  INFO 87094 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.65]
2024-09-11 19:29:05.688  INFO 87094 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-09-11 19:29:05.688  INFO 87094 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 507 ms
2024-09-11 19:29:05.786  INFO 87094 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2024-09-11 19:29:05.965  INFO 87094 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2024-09-11 19:29:06.236  INFO 87094 --- [           main] o.s.b.c.r.s.JobRepositoryFactoryBean     : No database type set, using meta data indicating: MYSQL
2024-09-11 19:29:06.246  INFO 87094 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : No TaskExecutor has been set, defaulting to synchronous executor.
2024-09-11 19:29:06.292  INFO 87094 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2024-09-11 19:29:06.299  INFO 87094 --- [           main] c.s.c.ChunkProcessingApplication         : Started ChunkProcessingApplication in 1.325 seconds (JVM running for 1.663)
2024-09-11 19:30:11.339  INFO 87094 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2024-09-11 19:30:11.340  INFO 87094 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2024-09-11 19:30:11.340  INFO 87094 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 0 ms
2024-09-11 19:30:11.409  INFO 87094 --- [nio-8080-exec-1] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=job1]] launched with the following parameters: [{param=1}]
2024-09-11 19:30:11.436  INFO 87094 --- [nio-8080-exec-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [chunkBasedStep1]
Chunk-processing Started
Product(productId=1, productName=Google Pixel 8, productCategory=Mobile Phones, productPrice=1200)
Product(productId=2, productName=iPad Mini, productCategory=Tablets, productPrice=800)
Product(productId=3, productName=Canon 1500D, productCategory=Cameras, productPrice=500)
Chunk-processing Ended
Chunk-processing Started
Product(productId=4, productName=LG 4K Ultra HD TV, productCategory=Televisions, productPrice=300)
Product(productId=5, productName=Goalkeeper Gloves, productCategory=Sports Accessories, productPrice=100)
Chunk-processing Ended
2024-09-11 19:30:11.459  INFO 87094 --- [nio-8080-exec-1] o.s.batch.core.step.AbstractStep         : Step: [chunkBasedStep1] executed in 22ms
2024-09-11 19:30:11.467  INFO 87094 --- [nio-8080-exec-1] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=job1]] completed with the following parameters: [{param=1}] and the following status: [COMPLETED] in 45ms

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
	public ItemReader<Product> flatFileItemReader() {
		FlatFileItemReader<Product> itemReader = new FlatFileItemReader<>();
		itemReader.setLinesToSkip(1);
		itemReader.setResource(new ClassPathResource("/data/Product_Details.csv"));

		DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames("product_id", "product_name", "product_category", "product_price");

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(new ProductFieldSetMapper());

		itemReader.setLineMapper(lineMapper);

		return itemReader;
	}
	
	@Bean
	public Step step1() {
		return this.stepBuilderFactory.get("chunkBasedStep1")
				.<Product,Product>chunk(3)
				.reader(flatFileItemReader())
				.writer(new ItemWriter<Product>() {

					@Override
					public void write(List<? extends Product> items) throws Exception {
						System.out.println("Chunk-processing Started");
						items.forEach(System.out::println);
						System.out.println("Chunk-processing Ended");
					}
				}).build();
	}
	
	@Bean
	public Job firstJob() {
		return this.jobBuilderFactory.get("job1")
				.start(step1())
				.build();
	}
}
```
