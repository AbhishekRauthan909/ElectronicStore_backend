package com.electronic.store.ElectronicStore.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
}