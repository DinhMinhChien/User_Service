package com.example.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressCreateRequest {

    @NotBlank(message = "Tên người nhận không được để trống")
    private String recipientName ;

    @NotBlank(message = "Số điện thoại nhận không được để trống")
    private String phoneNumber ;

    @NotBlank(message = "Mã tỉnh/thành phố không được để trống")
    private String provinceCode ;

    @NotBlank(message = "Tỉnh/Thành phố không được để trống")
    private String provinceName ;

    @NotBlank(message = "Mã xã/phường không đươ để trống")
    private String wardCode ;

    @NotBlank(message = "Xã/Phường không được để trống")
    private String wardName ;

    @NotBlank(message = "Địa chỉ chi tiết không được để trống")
    private String detailAddress ;


    private Boolean isDefault ;
}
