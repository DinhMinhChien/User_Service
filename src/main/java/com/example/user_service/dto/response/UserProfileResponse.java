package com.example.user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String username ;
    private String fullName ;
    private String phone ;
    private String email ;
    private List<AddressResponse> address ;

}
