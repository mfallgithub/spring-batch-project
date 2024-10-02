package com.springbatch.demo.processor;

import com.springbatch.demo.domain.Product;
import org.springframework.batch.item.ItemProcessor;

public class FilterProductItemProcessor implements ItemProcessor<Product, Product> {
    @Override
    public Product process(Product product) throws Exception {
        System.out.println("filterProductItemProcessor() executed!");
        return product.getProductPrice() > 100 ? product : null;
    }
}
