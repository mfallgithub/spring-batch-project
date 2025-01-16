package com.springbatch.demo.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Product {

    private Integer productId;
    private String productName;
    //@Pattern(regexp = "Mobile Phones|Tablets|Televisions|Sports Accessories")
    private String productCategory;
//    @Min(0)
//    @Max(100000)
    private Integer productPrice;

    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public @Min(0) @Max(100000) Integer getProductPrice() {
        return productPrice;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public void setProductPrice(@Min(0) @Max(100000) Integer productPrice) {
        this.productPrice = productPrice;
    }
}
