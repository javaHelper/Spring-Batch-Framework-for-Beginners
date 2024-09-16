package com.springbatch.config;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.springbatch.domain.Product;
import com.springbatch.domain.ProductFieldSetMapper;

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
