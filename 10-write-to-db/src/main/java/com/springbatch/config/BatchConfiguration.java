package com.springbatch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springbatch.domain.Product;
import com.springbatch.domain.ProductItemPreparedStatementSetter;
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
