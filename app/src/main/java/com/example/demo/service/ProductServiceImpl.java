package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.ProductEntity;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToModel)
                .orElseThrow(() -> new ResourceNotFoundException("Produit avec l'ID " + id + " non trouvé"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public Product createProduct(Product product) {
        ProductEntity entity = convertToEntity(product);
        ProductEntity saved = productRepository.save(entity);
        return convertToModel(saved);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit avec l'ID " + id + " non trouvé"));

        entity.setName(product.getName());
        entity.setPrice(product.getPrice());
        entity.setCountry(String.valueOf(product.getCountry()));

        ProductEntity updated = productRepository.save(entity);
        return convertToModel(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private Product convertToModel(ProductEntity entity) {
        return new Product(entity.getId(), entity.getName(), entity.getPrice(), Country.valueOf(entity.getCountry()));
    }

    private ProductEntity convertToEntity(Product model) {
        return new ProductEntity(null, model.getName(), model.getPrice(), String.valueOf(model.getCountry()));
    }
}