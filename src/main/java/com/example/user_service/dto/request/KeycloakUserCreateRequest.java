package com.example.user_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUserCreateRequest implements Serializable {
    private String username;
    private String email ;
    private String password ;
    private String role ;
}
