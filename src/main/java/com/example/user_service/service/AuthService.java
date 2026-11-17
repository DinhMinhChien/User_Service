package com.example.user_service.service;

import com.example.user_service.dto.request.*;

import java.util.Map;

public interface AuthService {
    void registerCustomer(CustomerRegisterRequest request) ;
    void registerSeller(SellerRegisterRequest request) ;
    Map<String,Object> login(UserLoginRequest request) ;
    Map<String,Object> refreshToken(TokenRefreshRequest request) ;
    void logout(String accessToken, String refreshToken);
    void changePassword(String username,String keycloakUserId,ChangePasswordRequest request) ;
}
