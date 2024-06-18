package com.electronic.store.ElectronicStore.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDto
{
    private String productId;
    private String title;
    private String description;
    private double price;
    private double discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String imageName;
    private CategoryDto category;
}
