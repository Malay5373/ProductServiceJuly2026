package dev.malay.productservicejuly26.services;

import dev.malay.productservicejuly26.dtos.FakeStoreProductDto;
import dev.malay.productservicejuly26.models.Category;
import dev.malay.productservicejuly26.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
//@Component or @Service or @Controller or @Repository all are same
// but in service class @Service is better option,
// for repository class is better @Repository like. But if any confusion is there then choose @Component .
public class FakeStoreProductService implements ProductService{

    private RestTemplate restTemplate;
    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Override
    public Product getSingleProduct(Long productId) {
        //call FakeStore to fetch the product with given id => HTTP call
        FakeStoreProductDto fakeStoreProductDto= restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + productId,
                FakeStoreProductDto.class);

        //convert FakeStoreProductDto into Product.
        return convertFakeProductDtoToProduct(fakeStoreProductDto);
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductDto[] fakeStoreProductDtos = restTemplate.getForObject(
                "https://fakestoreapi.com/products" , FakeStoreProductDto[].class
        );
        //Convert List of FakeStoreProductDto into List of Product
        List<Product> products = new ArrayList<>();
        for (FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtos) {
            products.add(convertFakeProductDtoToProduct(fakeStoreProductDto));
        }
        return products;
    }

    private Product convertFakeProductDtoToProduct(FakeStoreProductDto fakeStoreProductDto) {

        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setPrice(fakeStoreProductDto.getPrice());
        //Category we have used as object but from FakeStore we are getting only String value
        Category category = new Category();
        category.setDescription(fakeStoreProductDto.getDescription());
        product.setCategory(category);

        return product;
    }
}
