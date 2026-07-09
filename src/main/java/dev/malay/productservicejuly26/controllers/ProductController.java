package dev.malay.productservicejuly26.controllers;

import dev.malay.productservicejuly26.models.Product;
import dev.malay.productservicejuly26.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // http://localhost:8080/products/10
    @GetMapping("/{id}")
    public Product getProductByID(@PathVariable("id") Long id){
        return productService.getSingleProduct(id);
    }

    public List<Product> getAllProducts(){
        return new ArrayList<Product>();
    }
}
