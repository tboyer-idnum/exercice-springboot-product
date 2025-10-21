package com.example.demo.controller;

import com.example.demo.model.Country;
import com.example.demo.model.Product;

import com.example.demo.strategies.Tax.ContextTax;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Produits", description = "Opérations liées aux produits")
public class ProductController
{
    // Simulation d'une base de données
    private List<Product> products = new ArrayList<>();
    private Long nextId = 1L;

    public ProductController() {
        // Données de test
        this.products.add(new Product(nextId++, "Laptop", new BigDecimal(999.99), Country.FRANCE));
        this.products.add(new Product(nextId++, "Souris", new BigDecimal(29.99), Country.US));
        this.products.add(new Product(nextId++, "Clavier", new BigDecimal(79.99), Country.CANADA));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère un produit par son ID")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return this.products.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crée un produit")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        product.setId(nextId++);
        this.products.add(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/{id}/final-price")
    @Operation(summary = "Retourne le prix final d'un produit avec son ID")
    public ResponseEntity getFinalPriceOfProduct(@PathVariable Long id) {
        ResponseEntity response;

        try {
            BigDecimal finalPrice;
            Optional<ResponseEntity<Product>> entity = this.products.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .map(ResponseEntity::ok);
            Product product = entity
                    .get()
                    .getBody();

            ContextTax contextTax = new ContextTax();
            contextTax.setCountry(product.getCountry());
            BigDecimal taxPrice = contextTax.calculateTax(product);
            finalPrice = product.getPrice()
                .add(taxPrice);

            response = ResponseEntity.ok(finalPrice.doubleValue());
        } catch (NoSuchElementException e) {
            response = ResponseEntity.notFound().build();
        }

        return response;
    }
}