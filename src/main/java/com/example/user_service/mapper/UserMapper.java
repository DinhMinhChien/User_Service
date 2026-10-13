package com.example.user_service.mapper;

import com.example.user_service.dto.request.UserRegisterRequest;
import com.example.user_service.dto.request.UserSaveRequest;
import com.example.user_service.dto.response.AddressResponse;
import com.example.user_service.dto.response.UserProfileResponse;
import com.example.user_service.entity.Address;
import com.example.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserSaveRequest toUserSave(UserRegisterRequest request) ;

    // 1. Ánh xạ từ User và List<Address> sang UserProfileResponse
    @Mapping(target = "address", source = "addresses")
    UserProfileResponse toProfileResponse(User user, List<Address> addresses);

    // 2. Định nghĩa cách ánh xạ 1 Address sang 1 AddressResponse
    @Mapping(target = "fullAddress", expression = "java(buildFullAddress(address))")
    AddressResponse toAddressResponse(Address address);

    // 3. Hàm tự định nghĩa logic nối chuỗi (MapStruct sẽ tự gọi hàm này)
    default String buildFullAddress(Address address) {
        if (address == null) {
            return null;
        }
        // Nối chuỗi với dấu phẩy và khoảng trắng chuẩn
        return address.getAddressLine() + ", " + address.getCommune() + ", " + address.getProvince();
    }

}
