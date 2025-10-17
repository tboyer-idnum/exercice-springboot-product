package com.example.demo.strategies.Tax;

import com.example.demo.model.Product;

import java.math.BigDecimal;

public class FranceTaxStrategy implements TaxStrategy
{
    private BigDecimal tax = new BigDecimal(0.2);

    public BigDecimal calculateTax(Product product) {
        return product.getPrice().multiply(this.tax);
    }
}
