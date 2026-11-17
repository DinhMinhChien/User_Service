package com.example.user_service.service.implement;

import com.example.user_service.dto.request.ProfileUpdateRequest;
import com.example.user_service.dto.request.UserAddressCreateRequest;
import com.example.user_service.dto.response.UserAddressResponse;
import com.example.user_service.dto.response.UserProfileResponse;
import com.example.user_service.entity.UserAddress;
import com.example.user_service.entity.User;
import com.example.user_service.exception.BusinessException;
import com.example.user_service.mapper.UserAddressMapper;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.UserAddressRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository ;
    private final UserAddressRepository userAddressRepository;
    private final UserMapper userMapper ;
    private final UserAddressMapper userAddressMapper ;

    @Override
    public UserProfileResponse getProfile(String username) {
        // 1. Lấy dữ liệu
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BusinessException("Tài khoản có tên đăng nhập " + username +" không tồn tại !"));

        UserAddress userAddressesDefault = userAddressRepository
                .findUserAddressByUserIdAndIsDefaultAndDeleted(
                    user.getId(),
                    true,
                    false
                );

        // 2. Build response

        UserAddressResponse userAddressResponse = userAddressMapper.toResponse(userAddressesDefault) ;
        UserProfileResponse response = new UserProfileResponse();

        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhoneNumber());
        response.setAddressDefault(userAddressResponse);

        return response ;
    }

    @Override
    @Transactional
    public void updateProfile(String username, ProfileUpdateRequest request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BusinessException("Tài khoản có tên đăng nhập " + username +" không tồn tại !"));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user) ;

        UserAddress userAddressDefault = userAddressRepository.findUserAddressByUserIdAndIsDefaultAndDeleted(
                user.getId(),
                true,
                false
        ) ;
        if (userAddressDefault == null) return;

        userAddressDefault.setProvinceCode(request.getProvinceCode());
        userAddressDefault.setProvinceName(request.getProvinceName());
        userAddressDefault.setWardCode(request.getWardCode());
        userAddressDefault.setWardName(request.getWardName());
        userAddressDefault.setDetailAddress(request.getDetailAddress());

        userAddressRepository.save(userAddressDefault) ;
    }

    @Override
    @Transactional
    public void createAddress(String username, UserAddressCreateRequest request) {
        UserAddress userAddress = new UserAddress();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BusinessException("Tài khoản có tên đăng nhập " + username +" không tồn tại !"));

        userAddress.setUserId(user.getId());
        userAddress.setRecipientName(request.getRecipientName());
        userAddress.setPhoneNumber(request.getPhoneNumber());
        userAddress.setProvinceCode(request.getProvinceCode());
        userAddress.setProvinceName(request.getProvinceName());
        userAddress.setWardCode(request.getWardCode());
        userAddress.setWardName(request.getWardName());
        userAddress.setDetailAddress(request.getDetailAddress());
        if (request.getIsDefault()) {
            UserAddress currentUserAddressDefault = userAddressRepository
                    .findUserAddressByUserIdAndIsDefaultAndDeleted(
                        user.getId(),
                        true,
                        false
                    );
            if (currentUserAddressDefault != null) {
                currentUserAddressDefault.setIsDefault(false);
                userAddressRepository.save(currentUserAddressDefault) ;
            }
        }
        userAddress.setIsDefault(request.getIsDefault());

        userAddressRepository.save(userAddress);
    }
}
