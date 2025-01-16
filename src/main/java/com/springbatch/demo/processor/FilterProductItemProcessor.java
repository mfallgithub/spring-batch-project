package com.springbatch.demo.processor;

import com.springbatch.demo.domain.Product;
import org.springframework.batch.item.ItemProcessor;

public class FilterProductItemProcessor implements ItemProcessor<Product, Product> {
    @Override
    public Product process(Product item) throws Exception {
        System.out.println("filterProductItemProcessor() executed for product " +item.getProductId());
//        return item.getProductPrice() > 100 ? item : null;
        return item;
    }
}
