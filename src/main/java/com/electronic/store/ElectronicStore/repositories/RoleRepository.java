package com.electronic.store.ElectronicStore.repositories;

import com.electronic.store.ElectronicStore.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,String>
{
    Role findByName(String name);
}