package com.springbatch.demo.config;

import com.springbatch.demo.domain.OSProduct;
import com.springbatch.demo.domain.Product;
import com.springbatch.demo.domain.ProductFieldSetMapper;
import com.springbatch.demo.domain.ProductRowMapper;
import com.springbatch.demo.exception.MyException;
import com.springbatch.demo.listener.*;
import com.springbatch.demo.processor.FilterProductItemProcessor;
import com.springbatch.demo.processor.TransformProductItemProcessor;
import com.springbatch.demo.reader.ProductNameItemReader;
import com.springbatch.demo.skippolicy.MySkipPolicy;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
public class BatchConfiguration {

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

//    @Bean
//    public JdbcBatchItemWriter<Product> jdbcBatchItemWriter() {
//        JdbcBatchItemWriter<Product> itemWriter = new JdbcBatchItemWriter<>();
//        itemWriter.setDataSource(dataSource);
//        itemWriter.setSql("insert into product_details_output values (:productId,:productName,:productCategory,:productPrice)");
//        //itemWriter.setItemPreparedStatementSetter(new ProductItemPreparedStatement());
//        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//        return itemWriter;
//    }

    @Bean
    public JdbcBatchItemWriter<OSProduct> jdbcBatchItemWriter() {
        JdbcBatchItemWriter<OSProduct> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("insert into os_product_details values (:productId,:productName,:productCategory,:productPrice,:taxPercent,:sku,:shippingRate)");
        //itemWriter.setItemPreparedStatementSetter(new ProductItemPreparedStatement());
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
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

//    @Bean
//    public ValidatingItemProcessor<Product> validateItemProcessor() {
//        ValidatingItemProcessor<Product> processor = new ValidatingItemProcessor<>((new ProductValidator()));
//        processor.setFilter(true);
//        return processor;
//    }

    @Bean
    public BeanValidatingItemProcessor<Product> ValidateItemProcessor() {
        BeanValidatingItemProcessor<Product> beanValidatingItemProcessor = new BeanValidatingItemProcessor<>();
        //beanValidatingItemProcessor.setFilter(true);
        return beanValidatingItemProcessor;
    }

    @Bean
    public CompositeItemProcessor<Product, OSProduct> itemProcessor() {
        CompositeItemProcessor<Product, OSProduct> compositeItemProcessor = new CompositeItemProcessor<>();
        List itemProcessors = new ArrayList<>();
        itemProcessors.add(ValidateItemProcessor());
        itemProcessors.add(filterProductItemProcessor());
        itemProcessors.add(transformProductItemProcessor());
        compositeItemProcessor.setDelegates(itemProcessors);
        return compositeItemProcessor;
    }

    @Bean
    public MyChunkListener myChunkListener() {
        return new MyChunkListener();
    }

    @Bean
    public MyItemReadListener myItemReadListener() {
        return new MyItemReadListener();
    }

    @Bean
    public MyItemProcessListener myItemProcessListener() {
        return new MyItemProcessListener();
    }

    @Bean
    public MyItemWriteListener myItemWriteListener() {
        return new MyItemWriteListener();
    }

    @Bean
    public MySkipListener mySkipListener() {
        return new MySkipListener();
    }

    @Bean
    public MySkipPolicy mySkipPolicy(){
        return new MySkipPolicy();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("chunkBasedStep1", jobRepository)
                .<Product, OSProduct>chunk(3, transactionManager)
                .reader(flatFileItemReader())
                .processor(itemProcessor())
                .writer(jdbcBatchItemWriter())
                .faultTolerant()
                .retry(MyException.class)
                .retryLimit(4)
                .listener(mySkipListener())
               .listener(myChunkListener())
//                .listener(myItemReadListener())
//                .listener(myItemProcessListener())
//                .listener(myItemWriteListener())
                .build();
    }

    @Bean
    public Job firstJob(JobRepository jobRepository, Step step1) throws Exception {
        return new JobBuilder("job1", jobRepository)
                .start(step1)
                .build();

    }

}
