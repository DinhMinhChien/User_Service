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
public class UserRegisterRequest {

    @NotBlank(message = "username không được để trông")
    private String username ;

    @NotBlank(message = "password không được để trống")
    private String password ;

    @NotBlank(message = "fullName không được để trống")
    private String fullName ;

    @NotBlank(message = "phone không được để trống")
    private String phone ;

    @NotBlank(message = "email không được để trống")
    private String email ;

    @NotBlank(message = "role không được để trống")
    private String role ;

    @NotBlank(message = "province không được để trống")
    private String province ;

    @NotBlank(message = "commune không được để trống")
    private String commune ;

    @NotBlank(message = "addressLine không được để trống")
    private String addressLine ;

}
