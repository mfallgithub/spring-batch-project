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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public ItemReader<Product> jdbcPagingItemReader() throws Exception {
        JdbcPagingItemReader<Product> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(dataSource);

        SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setDataSource(dataSource);
        factory.setSelectClause("select product_id, product_name, product_category, product_price");
        factory.setFromClause("from product_details");
        factory.setSortKey("product_id");

        itemReader.setQueryProvider(Objects.requireNonNull(factory.getObject()));
        itemReader.setRowMapper(new ProductRowMapper());
        itemReader.setPageSize(3);

        return itemReader;
    }

    @Bean
    public ItemWriter<Product> flatFileItemWriter() {

        FlatFileItemWriter<Product> itemWriter = new FlatFileItemWriter<>();
        itemWriter.setResource(new FileSystemResource("src/main/resources/data/Product_Details_Output.csv"));

        DelimitedLineAggregator<Product> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<Product> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"productId", "productName", "productCategory", "productPrice"});

        lineAggregator.setFieldExtractor(fieldExtractor);

        itemWriter.setLineAggregator(lineAggregator);

        return itemWriter;

    }

    @Bean
    public Step step1() throws Exception {
        return this.stepBuilderFactory.get("chunkBasedStep1")
                .<Product, Product>chunk(3)
                //.reader(flatFileItemReader())
                //.reader(jdbcCursorItemReader())
                .reader(jdbcPagingItemReader())
                .writer(flatFileItemWriter())
                .build();
    }

    @Bean
    public Job firstJob() throws Exception {
        return this.jobBuilderFactory.get("job1")
                .start(step1())
                .build();

    }

}
