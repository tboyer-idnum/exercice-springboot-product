package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.demo.strategies.Tax.ContextTax;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Tag(name = "Produits", description = "Opérations liées aux produits")
public class ProductController
{
    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "Récupère un produit par son tous les produits")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère un produit par son ID")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    @Operation(summary = "Crée un produit")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @GetMapping("/{id}/final-price")
    @Operation(summary = "Retourne le prix final d'un produit avec son ID")
    public ResponseEntity getFinalPriceOfProduct(@PathVariable Long id) {
        ResponseEntity response;

        try {
            BigDecimal finalPrice;
            Product product = productService.getProductById(id);

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

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}