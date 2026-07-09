package dev.malay.productservicejuly26.services;

import dev.malay.productservicejuly26.dtos.FakeStoreProductDto;
import dev.malay.productservicejuly26.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service           //@Component or @Service or @Controller or @Repository all are same but in service class @Service is better option, for repository class is better @Repository like. But if it create confusion then choose @Component .
public class FakeStoreProductService implements ProductService{

    private RestTemplate restTemplate;
    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Override
    public Product getSingleProduct(Long productId) {
        //call FakeStore to fetch the product with given id => HTTP call
        FakeStoreProductDto fakeStoreProductDto= restTemplate.getForObject("https://fakestoreapi.com/products/" + productId, FakeStoreProductDto.class);
        return new Product();
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }
}
