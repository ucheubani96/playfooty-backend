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
public class SuccessResponseDTO {
    private Integer status;

    private String message;

    private Object data;

    private Instant timestamp;
}
