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

    public Integer getTaxPercent() {
        return taxPercent;
    }

    public String getSku() {
        return sku;
    }

    public Integer getShippingRate() {
        return shippingRate;
    }

    public void setTaxPercent(Integer taxPercent) {
        this.taxPercent = taxPercent;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setShippingRate(Integer shippingRate) {
        this.shippingRate = shippingRate;
    }
}
