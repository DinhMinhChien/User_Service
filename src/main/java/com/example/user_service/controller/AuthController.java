package com.example.user_service.controller;

import com.example.user_service.dto.request.*;
import com.example.user_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/customer")
    public ResponseEntity<String> registerCustomer(@RequestBody @Valid CustomerRegisterRequest request) {
        authService.registerCustomer(request) ;
        return ResponseEntity.ok("Đăng ký người mua thành công !") ;
    }

    @PostMapping("/register/seller")
    public ResponseEntity<String> registerSeller(@RequestBody @Valid SellerRegisterRequest request) {
        authService.registerSeller(request);
        return ResponseEntity.ok(("Đăng ký thành công ! Vui lòng chờ quản trị viên kiểm duyệt thông tin người bán")) ;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody UserLoginRequest request) {
        Map<String,Object> response = authService.login(request) ;
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String,Object>> refreshToken(@RequestBody TokenRefreshRequest request) {
        Map<String,Object> response = authService.refreshToken(request) ;
        return ResponseEntity.ok(response) ;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody @Valid UserLogoutRequest request) {
        // Trực tiếp truyền 2 token xuống service
        authService.logout(request.getAccessToken(), request.getRefreshToken());
        return ResponseEntity.ok("Đăng xuất thành công!");
    }

}
