package com.electronic.store.ElectronicStore.dtos;

import com.electronic.store.ElectronicStore.entities.OrderItems;
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
public class OrderDto
{
    private String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    private double orderAmount;
    private String billingName;
    private String billingAddress;
    private String billingPhone;
    private Date orderDate=new Date();
    private Date delivered=null;
    private List<OrderItemDto> orderItems=new ArrayList<>();
}
