package dev.malay.productservicejuly26.services;

import dev.malay.productservicejuly26.exceptions.ProductNotFoundException;
import dev.malay.productservicejuly26.models.Product;
import dev.malay.productservicejuly26.repositories.CategoryRepository;
import dev.malay.productservicejuly26.repositories.ProductRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Profile("database")
@Transactional
public class OwnProductService implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public OwnProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product createProduct(Product product) {
        product.setId(null);
        product.setCategory(resolveCategory(product));
        return productRepository.save(product);
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with productId: " + productId));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> searchProducts(String title, String category, Pageable pageable) {
        Specification<Product> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();
        if (title != null && !title.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                            "%" + title.toLowerCase(Locale.ROOT) + "%"));
        }
        if (category != null && !category.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("category").get("name")),
                            "%" + category.toLowerCase(Locale.ROOT) + "%"));
        }
        return productRepository.findAll(specification, pageable);
    }

    @Override
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found with productId: " + productId);
        }
        productRepository.deleteById(productId);
    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        Product existingProduct = getProductOrThrow(productId);
        existingProduct.setTitle(product.getTitle());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(resolveCategory(product));
        return productRepository.save(existingProduct);
    }

    @Override
    public Product replaceProduct(Long productId, Product product) {
        Product existingProduct = getProductOrThrow(productId);
        existingProduct.setTitle(product.getTitle());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(resolveCategory(product));
        return productRepository.save(existingProduct);
    }

    private Product getProductOrThrow(Long productId) {
        try {
            return getSingleProduct(productId);
        } catch (ProductNotFoundException exception) {
            throw exception;
        }
    }

    private dev.malay.productservicejuly26.models.Category resolveCategory(Product product) {
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            return product.getCategory() == null
                    ? null
                    : categoryRepository.save(product.getCategory());
        }
        return categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category not found with categoryId: " + product.getCategory().getId()));
    }
}
