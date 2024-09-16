package com.springbatch.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.springbatch.domain.OSProduct;
import com.springbatch.domain.Product;
import com.springbatch.domain.ProductRowMapper;
import com.springbatch.processor.FilterProductItemProcessor;
import com.springbatch.processor.TransformProductItemProcessor;

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
	public ItemWriter<Product> flatFileItemWriter() {
		FlatFileItemWriter<Product> itemWriter = new FlatFileItemWriter<>();
		itemWriter.setResource(new FileSystemResource("output/Product_Details_Output.csv"));
		
		DelimitedLineAggregator<Product> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setDelimiter(",");
		
		BeanWrapperFieldExtractor<Product> fieldExtractor = new BeanWrapperFieldExtractor<>();
		fieldExtractor.setNames(new String[] { "productId", "productName", "productCategory", "productPrice" });
		
		lineAggregator.setFieldExtractor(fieldExtractor);
		
		itemWriter.setLineAggregator(lineAggregator);
		return itemWriter;
	}
	
	@Bean
	public JdbcBatchItemWriter<OSProduct> jdbcBatchItemWriter() {
		JdbcBatchItemWriter<OSProduct> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setSql("insert into os_product_details values (:productId, :productName, :productCategory, :productPrice, :taxPercent, :sku, :shippingRate)");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<OSProduct>());
		return itemWriter;
	}
	
	@Bean
	public ItemProcessor<Product, Product> filterProductItemProcessor() {
		return new FilterProductItemProcessor();
	}
	
	@Bean
	public ItemProcessor<Product, OSProduct> transformProductItemProcessor() {
		return new TransformProductItemProcessor();
	}
	
	@Bean
	public BeanValidatingItemProcessor<Product> validateProductItemProcessor() {
		BeanValidatingItemProcessor<Product> beanValidatingItemProcessor = new BeanValidatingItemProcessor<>();
		beanValidatingItemProcessor.setFilter(true);
		return beanValidatingItemProcessor;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public CompositeItemProcessor<Product, OSProduct> itemProcessor() {
		CompositeItemProcessor<Product, OSProduct> itemProcessor = new CompositeItemProcessor<>();
		List itemProcessors = new ArrayList();
		itemProcessors.add(validateProductItemProcessor());
		itemProcessors.add(filterProductItemProcessor());
		itemProcessors.add(transformProductItemProcessor());
		itemProcessor.setDelegates(itemProcessors);
		return itemProcessor;
	}
	
	@Bean
	public Step step1() throws Exception {
		return this.stepBuilderFactory.get("chunkBasedStep1")
				.<Product,OSProduct>chunk(3)
				.reader(jdbcPagingItemReader())
				.processor(itemProcessor())
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
