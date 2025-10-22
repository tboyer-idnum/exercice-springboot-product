package com.example.demo.integrationsTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.entity.ProductEntity;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductEntity testProduct, testProduct2;

    @BeforeEach
    void setUp() {
        // Nettoie la base de données avant chaque test
        productRepository.deleteAll();

        // Crée et insère un produit de test en base de données
        testProduct = new ProductEntity();
        testProduct.setName("Laptop");
        testProduct.setPrice(new BigDecimal("999.99"));
        testProduct.setCountry("FRANCE");

        // Crée et insère un produit de test en base de données
        testProduct2 = new ProductEntity();
        testProduct2.setName("Keyboard");
        testProduct2.setPrice(new BigDecimal("30.0"));
        testProduct2.setCountry("US");

        testProduct = productRepository.save(testProduct);
        testProduct2 = productRepository.save(testProduct2);
    }

    @Test
    void testGetProductById_found() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", testProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testProduct.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo("Laptop")))
                .andExpect(jsonPath("$.price", is(999.99)))
                .andExpect(jsonPath("$.country", equalTo("FRANCE")));
    }

    @Test
    void testGetProductById_notFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("non trouvé")));
    }

    @Test
    void testGetAllProducts() throws Exception {
        // Arrange : insert un deuxième produit
        ProductEntity product2 = new ProductEntity();
        product2.setName("Mouse");
        product2.setPrice(new BigDecimal("29.99"));
        product2.setCountry("FRANCE");
        productRepository.save(product2);

        // Act & Assert
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", equalTo("Laptop")))
                .andExpect(jsonPath("$[1].name", equalTo("Mouse")));
    }

    @Test
    void testCreateProduct() throws Exception {
        // Arrange
        ProductEntity newProduct = new ProductEntity();
        newProduct.setName("Keyboard");
        newProduct.setPrice(new BigDecimal("79.99"));
        newProduct.setCountry("US");

        // Act & Assert
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("Keyboard")))
                .andExpect(jsonPath("$.price", is(79.99)))
                .andExpect(jsonPath("$.country", equalTo("US")))
                .andExpect(jsonPath("$.id", notNullValue()));

        // Vérify that product is saved in database
        assert productRepository.count() == 2;
    }

    @Test
    void testUpdateProduct() throws Exception {
        // Arrange
        ProductEntity updatedProduct = new ProductEntity();
        updatedProduct.setName("Updated Laptop");
        updatedProduct.setPrice(new BigDecimal("1099.99"));
        updatedProduct.setCountry("CANADA");

        // Act & Assert
        mockMvc.perform(put("/api/products/{id}", testProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testProduct.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo("Updated Laptop")))
                .andExpect(jsonPath("$.price", is(1099.99)))
                .andExpect(jsonPath("$.country", equalTo("CANADA")));
    }

    @Test
    void testDeleteProduct() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/products/{id}", testProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify product is deleted from database
        assert productRepository.count() == 0;
    }

    @Test
    void testGetProductFinalPrice() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/products/{id}/final-price", testProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1199.988"));

        // Act & Assert
        mockMvc.perform(get("/api/products/{id}/final-price", testProduct2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("36.0"));
    }
}