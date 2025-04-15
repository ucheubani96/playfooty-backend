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

    @Builder.Default
    private Object data = null;

    @Builder.Default
    private Instant timestamp = Instant.now();
}
