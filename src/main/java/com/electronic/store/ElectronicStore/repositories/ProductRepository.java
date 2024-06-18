package com.electronic.store.ElectronicStore.repositories;
import com.electronic.store.ElectronicStore.entities.Category;
import com.electronic.store.ElectronicStore.entities.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Products,String>
{
    Page<Products> findByTitleContaining(String title,Pageable pageable);
    Page<Products> findByLiveTrue(Pageable pageable);

    Page<Products> findByCategory(Category category,Pageable pageable);
}
