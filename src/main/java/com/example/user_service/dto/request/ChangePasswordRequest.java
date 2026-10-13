package com.example.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "oldPassword không được để trống")
    private String oldPassword ;

    @NotBlank(message = "newPassword không được để trống")
    private String newPassword ;
}
