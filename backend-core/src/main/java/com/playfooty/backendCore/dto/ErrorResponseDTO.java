package com.playfooty.backendCore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@Data
public class ErrorResponseDTO {
    private Integer status;

    private String error;

    private String message;

    private Instant timestamp;
}
