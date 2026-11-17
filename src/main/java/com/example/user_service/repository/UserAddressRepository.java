package com.example.user_service.repository;

import com.example.user_service.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserAddressRepository extends JpaRepository<UserAddress,String> {
    UserAddress findUserAddressByUserIdAndIsDefaultAndDeleted(String userId, Boolean isDefault, Boolean deleted);

    List<UserAddress> findAllByUserIdAndDeleted(String userId, Boolean deleted);
}
