package com.example.demo.strategies.Tax;

import com.example.demo.model.Country;
import com.example.demo.model.Product;

import java.math.BigDecimal;

public class ContextTax
{
    private Country country;
    private TaxStrategy taxStrategy;

    public ContextTax() {
        this.country = null;
        this.taxStrategy = null;
    }

    public ContextTax(Country country) {
        this.country = country;
        this.taxStrategy = null;
    }

    public TaxStrategy getTaxStrategy() {
        return taxStrategy;
    }

    public void setTaxStrategy(TaxStrategy taxStrategy) {
        this.taxStrategy = taxStrategy;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public BigDecimal calculateTax(Product product) {
        this.initStrategy();
        return this.taxStrategy.calculateTax(product);
    }

    private void initStrategy() {
        if (this.taxStrategy == null) {
            this.taxStrategy = (TaxStrategy) switch (this.country) {
                case FRANCE -> new FranceTaxStrategy();
                case US -> new UsTaxStrategy();
                case CANADA -> new CanadaTaxStrategy();
                default -> throw new IllegalCountryException("Ce pays n'est pas géré");
            };
        }
    }
}
