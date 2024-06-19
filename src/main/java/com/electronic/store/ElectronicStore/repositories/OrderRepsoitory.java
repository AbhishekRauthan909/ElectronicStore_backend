package com.electronic.store.ElectronicStore.repositories;

import com.electronic.store.ElectronicStore.entities.Order;
import com.electronic.store.ElectronicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepsoitory extends JpaRepository<Order,String>
{
    List<Order> findByUser(User user);

}
