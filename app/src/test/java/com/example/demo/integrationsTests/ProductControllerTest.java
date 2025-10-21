package com.example.demo.integrationsTests;

import com.example.demo.model.Country;
import com.example.demo.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // pour sérialiser/désérialiser JSON

    @Test
    public void testGetProductById_found() throws Exception {
        // ID 1 est initialisé dans le contrôleur
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    public void testGetProductById_notFound() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateProduct_success() throws Exception {
        Product newProduct = new Product(null, "Webcam", new BigDecimal("49.99"), Country.FRANCE);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Webcam"));
    }

    @Test
    public void testGetFinalPriceOfProduct_found() throws Exception {
        // Produit avec ID 1 est en France
        mockMvc.perform(get("/api/products/1/final-price"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetFinalPriceOfProduct_notFound() throws Exception {
        mockMvc.perform(get("/api/products/999/final-price"))
                .andExpect(status().isNotFound());
    }
}