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
public class ProfileUpdateRequest {
    @NotBlank(message = "Họ và tên không để trống ")
    private String fullName ;

    @NotBlank(message = "Email không được để trống")
    private String email ;

    @NotBlank(message = "Số điện thoại khong để trống")
    private String phoneNumber ;

    @NotBlank(message = "Mã tỉnh / thành phố không để trống")
    private String provinceCode ;

    @NotBlank(message = "Tên tỉnh / thành phố không để trống ")
    private String provinceName ;

    @NotBlank(message = "Mã xã / phường không để trong")
    private String wardCode ;

    @NotBlank(message = "Tên xã / Phường không được để trống ")
    private String wardName ;

    @NotBlank(message = "Địa chỉ chi tiết không để trống")
    private String detailAddress ;
}
