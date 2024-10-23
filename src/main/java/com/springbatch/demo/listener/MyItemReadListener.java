package com.springbatch.demo.listener;

import com.springbatch.demo.domain.Product;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.OnReadError;

public class MyItemReadListener {

    @BeforeRead
    public void beforeRead() {
        System.out.println("BeforeRead() executed");
    }

    @OnReadError
    public void onReadError(Exception ex) {
        System.out.println("onReadError() executed");
    }

    @AfterRead
    public void afterRead(Product item) {
        System.out.println("AfterRead() executed for product: " + item.getProductId());
    }
}
