package com.example.user_service.controller;

import com.example.user_service.common.BaseResponse;
import com.example.user_service.dto.request.ProfileUpdateRequest;
import com.example.user_service.dto.request.UserAddressCreateRequest;
import com.example.user_service.dto.request.ChangePasswordRequest;
import com.example.user_service.dto.response.UserProfileResponse;
import com.example.user_service.service.AuthService;
import com.example.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService ;
    private final AuthService authService ;

    // GET : http://localhost:8008/v1/users/profile - Lấy profile người dùng
    @PreAuthorize("hasAnyRole('CUSTOMER','SELLER')")
    @GetMapping("/profile")
    public ResponseEntity<BaseResponse<UserProfileResponse>> getProfile(@RequestHeader("X-User-Name") String username) {
        UserProfileResponse response = userService.getProfile(username) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(response,"Lấy profile người dùng thành công")) ;
    }

    // PUT : http://localhost:8008/v1/users/profile - Cập nhật profile người dùng
    @PreAuthorize("hasAnyRole('CUSTOMER','SELLER','ADMIN')")
    @PutMapping("/profile")
    public ResponseEntity<BaseResponse<String>> updateProfile(
            @RequestHeader("X-User-Name") String username,
            @RequestBody ProfileUpdateRequest request
    ) {
        userService.updateProfile(username,request) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Cập nhật thông tin profile thành công"));
    }
    // POST : http://localhost:8000/v1/users/change-password - Đổi mật khẩu
    @PreAuthorize("hasAnyRole('CUSTOMER','SELLER')")
    @PostMapping("/change-password")
    public ResponseEntity<BaseResponse<String>> changePassword(
            @RequestHeader("X-User-Name") String username,
            @RequestHeader("X-Keycloak-User-Id") String keycloakUserId,
            @RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(username,keycloakUserId ,request) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Đổi mật khẩu thành công")) ;
    }

    // POST : http://localhost:8008/v1/users/addresses - Thêm địa chỉ
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @PostMapping("/addresses")
    public ResponseEntity<BaseResponse<String>> createAddress(
            @RequestHeader("X-User-Name") String username,
            @RequestBody @Valid UserAddressCreateRequest request) {
        userService.createAddress(username,request) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Thêm địa chỉ cho người dùng thành công")) ;

    }


}
