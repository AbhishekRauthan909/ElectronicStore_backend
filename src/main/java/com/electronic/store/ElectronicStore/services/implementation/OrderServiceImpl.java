package com.electronic.store.ElectronicStore.services.implementation;
import com.electronic.store.ElectronicStore.dtos.CreateOrderRequest;
import com.electronic.store.ElectronicStore.dtos.OrderDto;
import com.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.electronic.store.ElectronicStore.entities.*;
import com.electronic.store.ElectronicStore.exceptions.BadApiRequest;
import com.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.electronic.store.ElectronicStore.helper.Helper;
import com.electronic.store.ElectronicStore.repositories.CartRepository;
import com.electronic.store.ElectronicStore.repositories.OrderRepsoitory;
import com.electronic.store.ElectronicStore.repositories.UserRepository;
import com.electronic.store.ElectronicStore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepsoitory orderRepsoitory;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public OrderDto createOrder(CreateOrderRequest request)
    {
        //1.Get the userid from the request
        String userId= request.getUserId();
        String cartId= request.getCartId();
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with given id"));
        //Ge the cart
        Cart cart=cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("Cart of the given id not found"));
        List<CartItem> cartItems=cart.getItems();
        if(cartItems.size()==0)
        {
            throw new BadApiRequest("Invalid number of items in cart");
        }

        Order order=Order.builder()
                .billingName(request.getBillingName())
                .billingAddress(request.getBillingAddress())
                .billingPhone(request.getBillingPhone())
                .orderDate(new Date())
                .delivered(null)
                .orderStatus(request.getOrderStatus())
                .paymentStatus(request.getPaymentStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();
        //order items and amount

        AtomicReference<Double> total_amount = new AtomicReference<>(0.0);
        List<OrderItems> orderItems=cartItems.stream().map(cartItem -> {

            OrderItems orderItem=OrderItems.builder()
                    .quantity(cartItem.getQuantity())
                    .products(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            total_amount.updateAndGet(v -> Double.valueOf(v + orderItem.getTotalPrice()));
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setOrderAmount(total_amount.get());
        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder=orderRepsoitory.save(order);
        return mapper.map(savedOrder,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId)
    {
        Order order=orderRepsoitory.findById(orderId).orElseThrow(()->new ResourceNotFoundException("given id order is not available"));
        orderRepsoitory.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUsers(String userId)
    {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
        List<Order> orders=orderRepsoitory.findByUser(user);
        List<OrderDto> orderDtos=orders.stream().map(order -> mapper.map(order,OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }
    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir)
    {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page=orderRepsoitory.findAll(pageable);
        return Helper.getPageableResponse(page, OrderDto.class);
    }
}
