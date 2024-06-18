package com.electronic.store.ElectronicStore.services;

import com.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.electronic.store.ElectronicStore.dtos.CartDto;

public interface CartService
{
    //This is for adding item into the cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //This is for removing items from the cart
    void removeItemFromCart(String userId,int cartItem);

    //This is for removing all items from the cart
    void clearCart(String userId);
    CartDto getCartByUser(String userId);
}
