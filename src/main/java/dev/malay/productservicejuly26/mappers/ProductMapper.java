package dev.malay.productservicejuly26.mappers;

import dev.malay.productservicejuly26.dtos.CategoryRequest;
import dev.malay.productservicejuly26.dtos.CategoryResponse;
import dev.malay.productservicejuly26.dtos.ProductRequest;
import dev.malay.productservicejuly26.dtos.ProductResponse;
import dev.malay.productservicejuly26.models.Category;
import dev.malay.productservicejuly26.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setCategory(toCategory(request.getCategory()));
        return product;
    }

    public ProductResponse toResponse(Product product) {
        Category category = product.getCategory();
        CategoryResponse categoryResponse = category == null ? null
                : new CategoryResponse(category.getId(), category.getName(), category.getDescription());
        return new ProductResponse(product.getId(), product.getTitle(), product.getPrice(),
                categoryResponse, product.getCreatedAt(), product.getUpdatedAt());
    }

    private Category toCategory(CategoryRequest request) {
        if (request == null) {
            return null;
        }
        Category category = new Category();
        category.setId(request.getId());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return category;
    }
}
