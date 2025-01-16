package com.springbatch.demo.processor;

import com.springbatch.demo.domain.Product;
import com.springbatch.demo.exception.MyException;
import org.springframework.batch.item.ItemProcessor;

import java.util.Random;

public class FilterProductItemProcessor implements ItemProcessor<Product, Product> {
    @Override
    public Product process(Product item) throws Exception {
        System.out.println("filterProductItemProcessor() executed for product " +item.getProductId());
//        return item.getProductPrice() > 100 ? item : null;
        Random random= new Random();
        if(item.getProductPrice() == 500 && random.nextInt(3) == 2){
            System.out.println("Exception Thrown");
                throw new MyException("Test Exception");
        }
        return item;
    }
}
