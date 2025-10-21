package com.example.demo.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product
{
    private Long id;
    private String name;
    private BigDecimal price;
    private Country country;
}
