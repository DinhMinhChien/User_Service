package com.example.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegisterRequest {

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName ;

    @NotBlank(message = "Tên đăng nhập không được để trông")
    private String username ;

    @NotBlank(message = "Email không được để trống")
    private String email ;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phoneNumber ;

    @NotBlank(message = "password không được để trống")
    private String password ;
}
