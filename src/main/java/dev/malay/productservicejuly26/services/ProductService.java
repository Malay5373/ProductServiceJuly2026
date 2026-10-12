package dev.malay.productservicejuly26.services;

import dev.malay.productservicejuly26.exceptions.ProductNotFoundException;
import dev.malay.productservicejuly26.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Locale;
import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product getSingleProduct(Long productId) throws ProductNotFoundException;
    List<Product> getAllProducts();
    default Page<Product> searchProducts(String title, String category, Pageable pageable) {
        List<Product> products = getAllProducts().stream()
                .filter(product -> title == null || title.isBlank()
                        || product.getTitle().toLowerCase(Locale.ROOT).contains(title.toLowerCase(Locale.ROOT)))
                .filter(product -> category == null || category.isBlank()
                        || product.getCategory() != null && product.getCategory().getName() != null
                        && product.getCategory().getName().toLowerCase(Locale.ROOT)
                        .contains(category.toLowerCase(Locale.ROOT)))
                .toList();
        int start = Math.min((int) pageable.getOffset(), products.size());
        int end = Math.min(start + pageable.getPageSize(), products.size());
        return new PageImpl<>(products.subList(start, end), pageable, products.size());
    }
    public void deleteProduct(Long productId);
    public Product updateProduct(Long productId, Product product);
    public Product replaceProduct(Long productId, Product product);
}
