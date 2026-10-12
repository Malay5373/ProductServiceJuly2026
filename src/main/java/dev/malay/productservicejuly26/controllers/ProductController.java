package dev.malay.productservicejuly26.controllers;

import dev.malay.productservicejuly26.exceptions.ProductNotFoundException;
import dev.malay.productservicejuly26.dtos.ProductRequest;
import dev.malay.productservicejuly26.dtos.ProductResponse;
import dev.malay.productservicejuly26.dtos.ProductPageResponse;
import dev.malay.productservicejuly26.mappers.ProductMapper;
import dev.malay.productservicejuly26.models.Product;
import dev.malay.productservicejuly26.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        Product product = productService.createProduct(productMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toResponse(product));
    }

    //GET-> http://localhost:8080/products/10
    @GetMapping("/{id}")
    public ProductResponse getProductByID(@PathVariable("id") Long id) throws ProductNotFoundException {
        return productMapper.toResponse(productService.getSingleProduct(id));
    }

    // GET All-> https://localhost:8080/products
    @GetMapping()
    public Object getAllProducts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        if (title == null && category == null && page == null && size == null
                && pageable.getSort().equals(Sort.by(Sort.Direction.ASC, "id"))) {
            return productService.getAllProducts().stream().map(productMapper::toResponse).toList();
        }
        Page<Product> products = productService.searchProducts(title, category, pageable);
        return new ProductPageResponse(
                products.getContent().stream().map(productMapper::toResponse).toList(),
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages());
    }

    //DELETE-> https://localhost:8080/products/2
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
    }

    // PATCH-> https://localhost:8080/products/1
    @PatchMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductRequest request){
        return productMapper.toResponse(productService.updateProduct(id, productMapper.toEntity(request)));
    }

    //PUT-> https://localhost:8080/products/3
    @PutMapping("/{id}")
    public ProductResponse replaceProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductRequest request){
        return productMapper.toResponse(productService.replaceProduct(id, productMapper.toEntity(request)));
    }
}
