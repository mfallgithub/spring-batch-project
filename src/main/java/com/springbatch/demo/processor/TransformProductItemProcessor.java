package com.springbatch.demo.processor;

import com.springbatch.demo.domain.OSProduct;
import com.springbatch.demo.domain.Product;
import org.springframework.batch.item.ItemProcessor;

public class TransformProductItemProcessor implements ItemProcessor<Product, OSProduct> {

    @Override
    public OSProduct process(Product product) throws Exception {
        System.out.println("transformProductItemProcessor() executed for product" + product.getProductId());
        OSProduct osProduct = new OSProduct();
        osProduct.setProductId(product.getProductId());
        osProduct.setProductName(product.getProductName());
        osProduct.setProductCategory(product.getProductCategory());
        osProduct.setProductPrice(product.getProductPrice());
        osProduct.setTaxPercent(product.getProductCategory().equals("Sports Accessories") ? 5 : 18);
        osProduct.setSku(product.getProductCategory().substring(0, 3) + product.getProductId());
        osProduct.setShippingRate(product.getProductPrice() < 1000 ? 75 : 0);
//        if (product.getProductPrice()==500) {
//            throw new Exception("Test Exception");
//        }
        return osProduct;
    }
}
