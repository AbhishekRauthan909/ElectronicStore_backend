package com.electronic.store.ElectronicStore.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="orders")
public class Order
{
    @Id
    private String orderId;

    //1.Pending 2.Dispatched 3.Delviered
    private String orderStatus;

    //1.Paid 2.Not paid
    private String paymentStatus;

    private double orderAmount;

    private String billingName;

    @Column(length = 100)
    private String billingAddress;

    private String billingPhone;

    private Date orderDate;

    private Date delivered;

    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy ="order",fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItems> orderItems=new ArrayList<>();
}
