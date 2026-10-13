package com.example.user_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveRequest {
    private String username;
    private String fullName;
    private String phone ;
    private String email;
    private String role;
}
