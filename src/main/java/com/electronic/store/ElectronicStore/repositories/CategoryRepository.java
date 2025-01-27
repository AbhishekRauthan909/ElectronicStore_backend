package com.electronic.store.ElectronicStore.repositories;

import com.electronic.store.ElectronicStore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,String>
{
    List<Category> findByTitleContaining(String keywords);
}
