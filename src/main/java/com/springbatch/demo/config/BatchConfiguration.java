package com.springbatch.demo.config;

import com.springbatch.demo.domain.Product;
import com.springbatch.demo.domain.ProductFieldSetMapper;
import com.springbatch.demo.domain.ProductRowMapper;
import com.springbatch.demo.reader.ProductNameItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

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
        productList.add("product1");
        productList.add("product2");
        productList.add("product3");
        productList.add("product4");
        productList.add("product5");
        productList.add("product6");
        productList.add("product7");
        productList.add("product7");
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
        lineTokenizer.setDelimiter(",");
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
    public Step step1() {
        return this.stepBuilderFactory.get("chunkBasedStep1")
                .<Product, Product>chunk(3)
                //.reader(flatFileItemReader())
                .reader(jdbcCursorItemReader())
                .writer(items -> {
                    System.out.println("Chunk-processing started");
                    items.forEach(System.out::println);
                    System.out.println("Chunk-processing ended");
                }).build();
    }

    @Bean
    public Job firstJob() {
        return this.jobBuilderFactory.get("job1")
                .start(step1())
                .build();

    }

}
