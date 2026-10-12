package dev.malay.productservicejuly26.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseModel {
    private String title;
    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
