package com.example.user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private String fullAddress ;
    private Boolean isDefault ;
    private String recipientName ;
    private String recipientPhone ;
}
