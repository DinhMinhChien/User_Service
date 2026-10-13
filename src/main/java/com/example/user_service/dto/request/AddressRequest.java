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
public class AddressRequest {

    @NotBlank(message = "Tên người nhận không được để trống")
    private String recipientName ;

    @NotBlank(message = "Số điện thoại nhận không được để trống")
    private String recipientPhone ;

    @NotBlank(message = "Xã/phường không được để trống")
    private String commune ;

    @NotBlank(message = "Tỉnh thành không được để trống")
    private String province ;

    @NotBlank(message = "Địa chỉ chi tiết không được để trống")
    private String addressLine ;

    private boolean isDefault ;
}
