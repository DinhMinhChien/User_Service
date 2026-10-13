package com.example.user_service.controller;

import com.example.user_service.common.BaseResponse;
import com.example.user_service.dto.request.AddressRequest;
import com.example.user_service.dto.request.ChangePasswordRequest;
import com.example.user_service.dto.response.AddressResponse;
import com.example.user_service.dto.response.UserProfileResponse;
import com.example.user_service.service.AuthService;
import com.example.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService ;
    private final AuthService authService ;

    // GET : http://localhost:8000/v1/users/profile - Lấy thông tin người dùng
    @GetMapping("/profile")
    public ResponseEntity<BaseResponse<UserProfileResponse>> getProfile(Principal principal) {
        String username = principal.getName() ;
        UserProfileResponse response = userService.getProfile(username) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(response,"Tải dữ liệu nguời dùng thành công")) ;
    }

    // POST : http://localhost:8000/v1/users/change-password - Đổi mật khẩu
    @PostMapping("/change-password")
    public ResponseEntity<BaseResponse<String>> changePassword(
            Principal principal ,
            @RequestHeader("X-User-Id") String userUuid,
            @RequestBody @Valid ChangePasswordRequest request) {
        String username = principal.getName() ;
        authService.changePassword(username,userUuid ,request) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Đổi mật khẩu thành công")) ;
    }

    @PostMapping("/addresses")
    public ResponseEntity<BaseResponse<String>> insertAddress(
            @RequestHeader("X-User-Name") String username,
            @RequestBody @Valid AddressRequest request) {
        userService.insertAddress(username,request) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Thêm địa chỉ cho người dùng thành công")) ;

    }


}
