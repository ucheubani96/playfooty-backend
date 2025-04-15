package com.playfooty.backendCore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddUserRequestDto {
    @NotNull
    @NotBlank
    @Size(min=3, max=50)
    private String username;

    @Email
    @NotNull
    @NotBlank
    @Size(min=5, max=50)
    private String email;

    @NotNull
    @NotBlank
    @Size(min=6, max=32)
    private String password;

    @NotBlank
    @Size(min=6, max=100)
    private String image;
}
