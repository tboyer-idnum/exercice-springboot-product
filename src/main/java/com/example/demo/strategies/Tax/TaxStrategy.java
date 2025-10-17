package com.example.demo.strategies.Tax;

import com.example.demo.model.Product;

import java.math.BigDecimal;

public interface TaxStrategy
{
    BigDecimal calculateTax(Product produt);
}
