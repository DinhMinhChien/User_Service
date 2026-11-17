package com.example.user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressResponse {
    private String recipientName ;
    private String phoneNumber ;
    private String provinceName ;
    private String wardName ;
    private String detailAddress ;
    private Boolean isDefault ;
}
