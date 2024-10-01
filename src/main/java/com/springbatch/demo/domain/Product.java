package com.springbatch.demo.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    private Integer productId;
    private String productName;
    private String productCategory;
    private Integer productPrice;

}
