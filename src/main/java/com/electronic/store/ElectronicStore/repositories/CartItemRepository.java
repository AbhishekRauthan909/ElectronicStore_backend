package com.electronic.store.ElectronicStore.repositories;

import com.electronic.store.ElectronicStore.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer>
{

}
