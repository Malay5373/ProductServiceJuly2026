package dev.malay.productservicejuly26.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    private Long id;

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;
}
