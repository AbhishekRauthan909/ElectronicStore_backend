package com.electronic.store.ElectronicStore.entities;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="cart_items")
public class CartItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Products product;
    private int quantity;
    private double totalPrice;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;
}
