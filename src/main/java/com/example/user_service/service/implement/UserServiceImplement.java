package com.example.user_service.service.implement;

import com.example.user_service.dto.request.AddressRequest;
import com.example.user_service.dto.response.UserProfileResponse;
import com.example.user_service.entity.Address;
import com.example.user_service.entity.User;
import com.example.user_service.exception.BusinessException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.AddressRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository ;
    private final AddressRepository addressRepository ;
    private final UserMapper userMapper ;

    @Override
    public UserProfileResponse getProfile(String username) {
        // 1. Lấy dữ liệu
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BusinessException("User not found with username: " + username));

        List<Address> addresses = addressRepository.findAllByUsername(username);

        // 2. Build response
        UserProfileResponse response = userMapper.toProfileResponse(user,addresses) ;

        return response ;
    }

    @Override
    public void insertAddress(String username, AddressRequest request) {
        Address address = new Address();

        address.setUsername(username);
        address.setRecipientName(request.getRecipientName());
        address.setRecipientPhone(request.getRecipientPhone());
        address.setCommune(request.getCommune());
        address.setProvince(request.getProvince());
        address.setAddressLine(request.getAddressLine());

        if (request.isDefault()) {
            List<Address> addressList = addressRepository.findAllByUsername(username);
            addressList.forEach(a -> a.setIsDefault(false));
            addressRepository.saveAll(addressList) ;
        }
        address.setIsDefault(request.isDefault());

        addressRepository.save(address);
    }
}
