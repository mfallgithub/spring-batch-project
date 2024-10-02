package com.springbatch.demo.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    private Integer productId;
    private String productName;
    @Pattern(regexp = "Mobile Phones|Tablets|Televisions|Sports Accessories")
    private String productCategory;
    @Max(100000)
    private Integer productPrice;

}
