package dev.malay.productservicejuly26.services;

import dev.malay.productservicejuly26.models.Product;

import java.util.List;

public interface ProductService {
    Product getSingleProduct(Long productId);
    List<Product> getAllProducts();
}
