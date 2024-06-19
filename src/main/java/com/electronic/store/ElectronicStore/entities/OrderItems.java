package com.electronic.store.ElectronicStore.entities;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="order_items")
public class OrderItems
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;
    private int quantity;
    private double totalPrice;
    @ManyToOne
    @JoinColumn(name="product_id")
    private Products products;
    @ManyToOne
    private Order order;
}
