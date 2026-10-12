package dev.malay.productservicejuly26.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    @NotBlank(message = "Product title is required")
    private String title;

    @Positive(message = "Product price must be greater than zero")
    private double price;

    @Valid
    private CategoryRequest category;
}
