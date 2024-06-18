package com.electronic.store.ElectronicStore.dtos;

import com.electronic.store.ElectronicStore.entities.CartItem;
import com.electronic.store.ElectronicStore.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto
{
    private String cartId;
    private Date createsAt;
    private UserDto userDto;
    private List<CartItemDto> items=new ArrayList<>();
}
