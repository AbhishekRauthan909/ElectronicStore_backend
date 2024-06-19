package com.electronic.store.ElectronicStore.services;

import com.electronic.store.ElectronicStore.dtos.CreateOrderRequest;
import com.electronic.store.ElectronicStore.dtos.OrderDto;
import com.electronic.store.ElectronicStore.dtos.PageableResponse;
import java.util.List;

public interface OrderService
{
    //create order
    OrderDto createOrder(CreateOrderRequest request);
    //remove order
    void removeOrder(String orderId);

    //get order of the user
    List<OrderDto> getOrdersOfUsers(String userId);

    //get orders
    PageableResponse<OrderDto> getAllOrders(int pageNumber,int pageSize,String sortBy,String sortDir);
}
