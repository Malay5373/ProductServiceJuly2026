package dev.malay.productservicejuly26.controllers;

import dev.malay.productservicejuly26.exceptions.ProductNotFoundException;
import dev.malay.productservicejuly26.mappers.ProductMapper;
import dev.malay.productservicejuly26.models.Product;
import dev.malay.productservicejuly26.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(ProductMapper.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void createProductReturnsCreatedResponse() throws Exception {
        Product product = product("Laptop", 75000);
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Laptop","price":75000,"category":{"name":"Electronics"}}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Laptop"))
                .andExpect(jsonPath("$.price").value(75000));
    }

    @Test
    void invalidProductReturnsValidationError() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"","price":-1}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.title").exists())
                .andExpect(jsonPath("$.fieldErrors.price").exists());
    }

    @Test
    void missingProductReturnsNotFound() throws Exception {
        when(productService.getSingleProduct(99L))
                .thenThrow(new ProductNotFoundException("Product not found with productId: 99"));

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found with productId: 99"));
    }

    @Test
    void deleteProductReturnsNoContent() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listProductsSupportsPaginationSortingAndFilters() throws Exception {
        Product product = product("Laptop", 75000);
        when(productService.searchProducts(eq("top"), eq("Electronics"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(java.util.List.of(product), Pageable.ofSize(2), 1));

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "price,desc")
                        .param("title", "top")
                        .param("category", "Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Laptop"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void listProductsWithoutQueryParametersKeepsListResponse() throws Exception {
        Product product = product("Laptop", 75000);
        when(productService.getAllProducts()).thenReturn(java.util.List.of(product));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Laptop"));
    }

    private Product product(String title, double price) {
        Product product = new Product();
        product.setTitle(title);
        product.setPrice(price);
        return product;
    }
}
