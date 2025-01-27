package com.electronic.store.ElectronicStore.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category
{
    @Id
    @Column(name="id")
    private String categoryId;
    @Column(name="category_title",length=60,nullable = false)
    private String title;
    @Column(name="category_description",length=60)
    private String description;
    private String coverImage;
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Products> products=new ArrayList<>();
}
