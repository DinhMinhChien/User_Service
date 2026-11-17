package com.example.user_service.client.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopCreateRequest implements Serializable {

    @NotBlank(message = "Định danh chủ cửa hàng trên keycloak không được để trống")
    private String keycloakOwnerId ;

    @NotBlank(message = "Tên cửa hàng không được để trống")
    private String shopName ;

    @NotBlank(message = "Mô tả cửa hàng không được để trống ")
    private String description ;

    @NotBlank(message = "Tên người liên lạc không được để trống ")
    private String contactName ;

    @NotBlank(message = "Số liên lạc của shop không được để trống ")
    private String contactPhone ;

    @NotBlank(message = "Thông tin mã tỉnh không được để trống")
    private String provinceCode ;

    @NotBlank(message = "Thông tin tên tỉnh không được để trống")
    private String provinceName;

    @NotBlank(message = "Thông tin mã phường/xã không đươc để trống")
    private String wardCode ;

    @NotBlank(message = "Thông tin tên phường/xã không được để trống")
    private String wardName ;

    @NotBlank(message = "Địa chỉ chi tiết của cửa hàng không được để trống")
    private String detailAddress ;

}
