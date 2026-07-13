package dev.malay.productservicejuly26.controllers;

import dev.malay.productservicejuly26.models.Product;
import dev.malay.productservicejuly26.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //GET-> http://localhost:8080/products/10
    @GetMapping("/{id}")
    public Product getProductByID(@PathVariable("id") Long id){
        return productService.getSingleProduct(id);
    }

    // GET All-> https://localhost:8080/products
    @GetMapping()
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    //DELETE-> https://localhost:8080/products/2
    public void deleteProduct(Long id){

    }

    // PATCH-> https://localhost:8080/products/1
    @PostMapping("/{id}")
    public Product updateProduct(@PathVariable("id") Long id, @RequestBody Product product){
        return productService.updateProduct(id, product);
    }

    //PUT-> https://localhost:8080/products/3
    @PutMapping("/{id}")
    public Product replaceProduct(@PathVariable("id") Long id, @RequestBody Product product){
        return productService.replaceProduct(id, product);
    }
}
