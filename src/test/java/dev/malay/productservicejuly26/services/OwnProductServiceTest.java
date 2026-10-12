package dev.malay.productservicejuly26.services;

import dev.malay.productservicejuly26.exceptions.ProductNotFoundException;
import dev.malay.productservicejuly26.models.Category;
import dev.malay.productservicejuly26.models.Product;
import dev.malay.productservicejuly26.repositories.CategoryRepository;
import dev.malay.productservicejuly26.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private OwnProductService productService;

    @Test
    void createProductClearsIdResolvesCategoryAndSavesProduct() {
        Category category = category(7L, "Electronics");
        Product product = product(42L, "Laptop", 75000, category);
        when(categoryRepository.findById(7L)).thenReturn(Optional.of(category));
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertSame(product, result);
        assertNull(product.getId());
        assertSame(category, product.getCategory());
        verify(categoryRepository).findById(7L);
        verify(productRepository).save(product);
    }

    @Test
    void getSingleProductReturnsProductById() {
        Product product = product(1L, "Laptop", 75000, null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertSame(product, productService.getSingleProduct(1L));

        verify(productRepository).findById(1L);
    }

    @Test
    void getSingleProductThrowsWhenProductIsMissing() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getSingleProduct(99L));

        assertEquals("Product not found with productId: 99", exception.getMessage());
    }

    @Test
    void getAllProductsReturnsProductsFromRepository() {
        List<Product> products = List.of(product(1L, "Laptop", 75000, null));
        when(productRepository.findAll()).thenReturn(products);

        assertSame(products, productService.getAllProducts());

        verify(productRepository).findAll();
    }

    @Test
    void searchProductsUsesPageableAndSpecification() {
        List<Product> products = List.of(product(1L, "Laptop", 75000, null));
        PageRequest pageable = PageRequest.of(1, 2, Sort.by("price"));
        when(productRepository.findAll(
                org.mockito.ArgumentMatchers.<Specification<Product>>any(),
                org.mockito.ArgumentMatchers.eq(pageable)))
                .thenReturn(new PageImpl<>(products, pageable, 5));

        var result = productService.searchProducts("top", "Electronics", pageable);

        assertEquals(products, result.getContent());
        assertEquals(1, result.getNumber());
        assertEquals(5, result.getTotalElements());
        verify(productRepository).findAll(
                org.mockito.ArgumentMatchers.<Specification<Product>>any(),
                org.mockito.ArgumentMatchers.eq(pageable));
    }

    @Test
    void updateProductChangesExistingProductFields() {
        Category category = category(7L, "Electronics");
        Product existing = product(1L, "Old title", 100, null);
        Product changes = product(null, "New title", 200, category);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.findById(7L)).thenReturn(Optional.of(category));
        when(productRepository.save(existing)).thenReturn(existing);

        Product result = productService.updateProduct(1L, changes);

        assertSame(existing, result);
        assertEquals("New title", existing.getTitle());
        assertEquals(200, existing.getPrice());
        assertSame(category, existing.getCategory());
        verify(productRepository).save(existing);
    }

    @Test
    void replaceProductReplacesExistingProductFields() {
        Category category = category(8L, "Computers");
        Product existing = product(2L, "Old title", 100, null);
        Product replacement = product(null, "Replacement", 300, category);
        when(productRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(categoryRepository.findById(8L)).thenReturn(Optional.of(category));
        when(productRepository.save(existing)).thenReturn(existing);

        Product result = productService.replaceProduct(2L, replacement);

        assertSame(existing, result);
        assertEquals("Replacement", existing.getTitle());
        assertEquals(300, existing.getPrice());
        assertSame(category, existing.getCategory());
        verify(productRepository).save(existing);
    }

    @Test
    void deleteProductDeletesExistingProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProductThrowsWhenProductIsMissing() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(99L));

        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void createProductThrowsWhenCategoryIsMissing() {
        Category missingCategory = category(404L, "Unknown");
        Product product = product(1L, "Laptop", 75000, missingCategory);
        when(categoryRepository.findById(404L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(product));

        assertEquals("Category not found with categoryId: 404", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    private Product product(Long id, String title, double price, Category category) {
        Product product = new Product();
        product.setId(id);
        product.setTitle(title);
        product.setPrice(price);
        product.setCategory(category);
        return product;
    }

    private Category category(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }
}
