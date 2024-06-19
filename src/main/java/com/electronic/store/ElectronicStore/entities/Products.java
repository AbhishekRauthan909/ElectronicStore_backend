package com.electronic.store.ElectronicStore.entities;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="products")
public class Products
{
    @Id
    private String productId;
    private String title;
    @Column(length=10000)
    private String description;
    private double price;
    private double discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String imageName;
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
    @OneToMany(mappedBy ="products")
    private List<OrderItems> orderItems=new ArrayList<>();
}
