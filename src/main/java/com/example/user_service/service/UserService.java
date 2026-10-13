package com.example.user_service.service;

import com.example.user_service.dto.request.AddressRequest;
import com.example.user_service.dto.response.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(String username) ;
    void insertAddress(String username, AddressRequest request) ;
}
