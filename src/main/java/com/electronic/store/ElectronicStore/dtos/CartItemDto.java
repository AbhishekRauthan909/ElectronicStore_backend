package com.electronic.store.ElectronicStore.dtos;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto
{
    private int cartItemId;
    private ProductDto productDto;
    private int quantity;
    private double totalPrice;
}