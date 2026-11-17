package com.example.user_service.service;

import com.example.user_service.dto.request.ProfileUpdateRequest;
import com.example.user_service.dto.request.UserAddressCreateRequest;
import com.example.user_service.dto.response.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(String username) ;
    void updateProfile(String username, ProfileUpdateRequest request) ;
    void createAddress(String username, UserAddressCreateRequest request) ;
}
