package com.playfooty.backendCore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@Data
public class PaginationRequestDTO {

    @Min(value = 0, message = "Page must be a positive number")
    private Integer page = 0;

    @Min(value = 0, message = "Size must be a positive number")
    private Integer size = 10;

    @Size(min = 1, message = "Name cannot be empty if provided")
    private String sortBy = "created_at";

    @Size(min = 1, message = "Name cannot be empty if provided")
    private String sortDir = "asc";
}
