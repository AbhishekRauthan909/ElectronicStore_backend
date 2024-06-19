package com.electronic.store.ElectronicStore.dtos;

import com.electronic.store.ElectronicStore.entities.Order;
import com.electronic.store.ElectronicStore.entities.Products;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto
{
    private int orderItemId;
    private int quantity;
    private double totalPrice;
    private ProductDto products;
}
