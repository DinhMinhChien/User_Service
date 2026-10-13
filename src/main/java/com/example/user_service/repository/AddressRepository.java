package com.example.user_service.repository;

import com.example.user_service.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,String> {
    List<Address> findAllByUsername(String username);
}
