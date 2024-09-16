package com.springbatch.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.springbatch.domain.Product;
import com.springbatch.domain.ProductFieldSetMapper;
import com.springbatch.domain.ProductRowMapper;
import com.springbatch.reader.ProductNameItemReader;

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
	public ItemReader<String> itemReader() {
		List<String> productList = new ArrayList<>();
		productList.add("Product 1");
		productList.add("Product 2");
		productList.add("Product 3");
		productList.add("Product 4");
		productList.add("Product 5");
		productList.add("Product 6");
		productList.add("Product 7");
		productList.add("Product 8");
		return new ProductNameItemReader(productList);
	}
	
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
	public ItemReader<Product> jdbcCursorItemReader() {
		JdbcCursorItemReader<Product> itemReader = new JdbcCursorItemReader<>();
		itemReader.setDataSource(dataSource);
		itemReader.setSql("select * from product_details order by product_id");
		itemReader.setRowMapper(new ProductRowMapper());
		return itemReader;
	}
	
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
	public Step step1() throws Exception {
		return this.stepBuilderFactory.get("chunkBasedStep1")
				.<Product,Product>chunk(3)
				.reader(jdbcPagingItemReader())
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
	public Job firstJob() throws Exception {
		return this.jobBuilderFactory.get("job1")
				.start(step1())
				.build();
	}
}
