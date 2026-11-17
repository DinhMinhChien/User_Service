package com.example.user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String username ;
    private String role ;
    private String fullName ;
    private String email ;
    private String phone ;
    private UserAddressResponse addressDefault ;

}
