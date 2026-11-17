package com.example.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLogoutRequest {
    @NotBlank(message = "Access Token không được để trống")
    private String accessToken;

    @NotBlank(message = "Refresh Token không được để trống")
    private String refreshToken;
}
