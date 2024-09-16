package com.springbatch.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springbatch.domain.Product;
import com.springbatch.domain.ProductRowMapper;

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
	public ItemReader<Product> jdbcCursorItemReader() {
		JdbcCursorItemReader<Product> itemReader = new JdbcCursorItemReader<>();
		itemReader.setDataSource(dataSource);
		itemReader.setSql("select * from product_details order by product_id");
		itemReader.setRowMapper(new ProductRowMapper());
		return itemReader;
	}
	
	@Bean
	public Step step1() {
		return this.stepBuilderFactory.get("chunkBasedStep1")
				.<Product,Product>chunk(3)
				.reader(jdbcCursorItemReader())
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
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}
}
