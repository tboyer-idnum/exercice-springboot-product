package com.example.demo.unitTests.Tax;

import com.example.demo.model.Country;
import com.example.demo.model.Product;
import com.example.demo.strategies.Tax.CanadaTaxStrategy;
import com.example.demo.strategies.Tax.FranceTaxStrategy;
import com.example.demo.strategies.Tax.TaxStrategy;
import com.example.demo.strategies.Tax.UsTaxStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests des stratégies de taxation")
class TaxStrategyTest
{
    @Test
    @DisplayName("UsTaxStrategy devrait calculer 8% de taxe")
    void testUsTaxStrategy() {
        Product product = new Product(1L, "Free Product", new BigDecimal(100.0), Country.US);
        TaxStrategy usTax = new UsTaxStrategy();
        BigDecimal productTax = usTax.calculateTax(product);
        assertEquals(
            8.0,
            productTax.doubleValue()
        );
    }

    @Test
    @DisplayName("CanadaTaxStrategy devrait calculer 13% de taxe")
    void testCanadaTaxStrategy() {
        Product product = new Product(1L, "Free Product", new BigDecimal(100.0), Country.CANADA);
        TaxStrategy canadaTax = new CanadaTaxStrategy();
        BigDecimal productTax = canadaTax.calculateTax(product);
        assertEquals(
            13.0,
            productTax.doubleValue()
        );
    }

    @Test
    @DisplayName("FranceTaxStrategy devrait calculer 20% de taxe")
    void testFranceTaxStrategy() {
        Product product = new Product(1L, "Free Product", new BigDecimal(100.0), Country.FRANCE);
        TaxStrategy franceTax = new FranceTaxStrategy();
        BigDecimal productTax = franceTax.calculateTax(product);
        assertEquals(
            20.0,
            productTax.doubleValue()
        );
    }

    @Test
    @DisplayName("UsTaxStrategy avec produit à 0€ devrait retourner 0")
    void testUsTaxStrategyWithZeroPrice() {
        Product freeProduct = new Product(2L, "Free Product", new BigDecimal(0.0), Country.US);
        TaxStrategy usTax = new UsTaxStrategy();
        BigDecimal productTax = usTax.calculateTax(freeProduct);
        assertEquals(
            0.0,
            productTax.multiply(freeProduct.getPrice()).doubleValue()
        );
    }

    @Test
    @DisplayName("CanadaTaxStrategy avec produit cher devrait calculer correctement")
    void testCanadaTaxStrategyWithExpensiveProduct() {
        Product expensiveProduct = new Product(3L, "Expensive Product", new BigDecimal(1000000000.0), Country.CANADA);
        TaxStrategy canadaTax = new CanadaTaxStrategy();
        BigDecimal productTax = canadaTax.calculateTax(expensiveProduct);
        assertEquals(
            130000000.0,
            productTax.doubleValue()
        );
    }

    @Test
    @DisplayName("FranceTaxStrategy avec prix décimal devrait calculer précisément")
    void testFranceTaxStrategyWithDecimalPrice() {
        Product decimalProduct = new Product(4L, "Decimal Product", new BigDecimal(99.99), Country.FRANCE);
        TaxStrategy franceTax = new FranceTaxStrategy();
        BigDecimal productTax = franceTax.calculateTax(decimalProduct);
        assertEquals(
            19.998,
            productTax.doubleValue()
        );
    }
}