package com.example.user_service.mapper;

import com.example.user_service.dto.response.UserAddressResponse;
import com.example.user_service.entity.UserAddress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {
    UserAddressResponse toResponse(UserAddress userAddress) ;
}
