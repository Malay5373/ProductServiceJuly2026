package dev.malay.productservicejuly26.services;

import dev.malay.productservicejuly26.models.Product;

import java.util.List;

public interface ProductService {
    Product getSingleProduct(Long productId);
    List<Product> getAllProducts();
    public void deleteProduct(Long productId);
    public Product updateProduct(Long productId, Product product);
    public Product replaceProduct(Long productId, Product product);
}
