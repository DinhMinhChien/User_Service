package com.example.user_service.service;

import com.example.user_service.dto.request.ChangePasswordRequest;
import com.example.user_service.dto.request.UserLoginRequest;
import com.example.user_service.dto.request.UserRegisterRequest;

import java.util.Map;

public interface AuthService {
    void register(UserRegisterRequest request) ;
    Map<String,Object> login(UserLoginRequest request) ;
    void delete(String keyCloakId) ;
    void changePassword(String username,String userUuid,ChangePasswordRequest request) ;
}
