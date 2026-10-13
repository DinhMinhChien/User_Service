package com.example.user_service.controller;

import com.example.user_service.dto.request.UserLoginRequest;
import com.example.user_service.dto.request.UserRegisterRequest;
import com.example.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterRequest request) {
        authService.register(request) ;
        return ResponseEntity.ok("Register account success") ;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody UserLoginRequest request) {
        Map<String,Object> response = authService.login(request) ;
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{keyCloakId}")
    public ResponseEntity<String> delete(@PathVariable String keyCloakId) {
        authService.delete(keyCloakId) ;
        return ResponseEntity.ok("Delete user success") ;
    }

}
