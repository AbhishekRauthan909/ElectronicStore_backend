package com.electronic.store.ElectronicStore.controllers;

import com.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.electronic.store.ElectronicStore.dtos.CartDto;
import com.electronic.store.ElectronicStore.payload.ApiResponseMessage;
import com.electronic.store.ElectronicStore.services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController
{

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemtoCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request,
                                                 HttpSession httpSession)
    {
        CartDto cartDto=cartService.addItemToCart(userId,request);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/item/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId,@PathVariable int itemId)
    {
        cartService.removeItemFromCart(userId,itemId);
        ApiResponseMessage msg=ApiResponseMessage.builder().
                message("Item is removed").
                success(true).
                status(HttpStatus.OK).build();
        return new ResponseEntity<>(msg,HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId)
    {
        cartService.clearCart(userId);
        ApiResponseMessage msg=ApiResponseMessage.builder().
                message("Cart is cleared").
                success(true).
                status(HttpStatus.OK).build();
        return new ResponseEntity<>(msg,HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId)
    {
        CartDto cartDto=cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto,HttpStatus.OK);
    }

}
