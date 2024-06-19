package com.electronic.store.ElectronicStore.controllers;


import com.electronic.store.ElectronicStore.dtos.CreateOrderRequest;
import com.electronic.store.ElectronicStore.dtos.OrderDto;
import com.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.electronic.store.ElectronicStore.payload.ApiResponseMessage;
import com.electronic.store.ElectronicStore.services.OrderService;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController
{

    @Autowired
    private OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request)
    {

        OrderDto orderDto=orderService.createOrder(request);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId)
    {
        orderService.removeOrder(orderId);
        ApiResponseMessage msg=ApiResponseMessage.builder().status(HttpStatus.OK)
                .message("Order is removed")
                .success(true)
                .build();
        return new ResponseEntity<>(msg,HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderOfUser(@PathVariable String userId)
    {
        List<OrderDto> orderDtos=orderService.getOrdersOfUsers(userId);
        return new ResponseEntity<>(orderDtos,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
            @RequestParam(value="pageNumber",defaultValue ="0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "ASC",required = false) String sortDir
    )
    {
        PageableResponse<OrderDto> orders=orderService.getAllOrders(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }


}
