package com.example.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SellerRegisterRequest implements Serializable {

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

    @NotBlank(message = "Tên shop không để trống")
    private String shopName ;

    @NotBlank(message = "Mô tả về shop không bỏ trống")
    private String descriptionShop ;

    @NotBlank(message = "Mã tỉnh thành không được để trống")
    private String provinceCode ;

    @NotBlank(message = "Tên tỉnh thành không được để trống")
    private String provinceName ;

    @NotBlank(message = "Mã phường/xã không được để trống ")
    private String wardCode ;

    @NotBlank(message = "Tên phường/xã không được để trống")
    private String wardName ;

    @NotBlank(message = "Địa chỉ chi tiết không được để trống")
    private String detailAddress ;
}
