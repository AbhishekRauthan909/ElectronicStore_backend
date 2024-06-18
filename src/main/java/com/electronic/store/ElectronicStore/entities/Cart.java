package com.electronic.store.ElectronicStore.entities;
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
@Entity
@Table(name="cart")
public class Cart
{
    @Id
    private String cartId;
    private Date createsAt; //this is the time at which this cart is created.
    @OneToOne //this is for specifying this cart belong to which user.
    @JoinColumn(name="user_id")
    private User user;
    @OneToMany(mappedBy="cart",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<CartItem> items=new ArrayList<>();
}
