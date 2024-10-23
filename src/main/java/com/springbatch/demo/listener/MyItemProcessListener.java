package com.springbatch.demo.listener;

import com.springbatch.demo.domain.OSProduct;
import com.springbatch.demo.domain.Product;
import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.annotation.OnProcessError;

public class MyItemProcessListener {
    @BeforeProcess
    public void beforeProcess(Product item) {
        System.out.println("BeforeProcess() executed for product: " + item.getProductId());
    }

    @AfterProcess
    public void afterProcess(Product item, OSProduct result) {
        System.out.println("AfterProcess() executed for product: " + item.getProductId());
    }

    @OnProcessError
    public void onProcessError(Product item, Exception e) {
        System.out.println("onProcessError() executed for product: " + item.getProductId());
    }
}
