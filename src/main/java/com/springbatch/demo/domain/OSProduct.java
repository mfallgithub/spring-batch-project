package com.springbatch.demo.domain;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OSProduct extends Product {
    private Integer taxPercent;
    private String sku;
    private Integer shippingRate;

}
