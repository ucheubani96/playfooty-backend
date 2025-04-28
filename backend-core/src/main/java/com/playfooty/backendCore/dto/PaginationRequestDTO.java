package com.playfooty.backendCore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
//@Getter
//@Setter
public class PaginationRequestDTO {

    @Min(value = 0, message = "Page must be a positive number")
    private Integer page;

    @Min(value = 0, message = "Size must be a positive number")
    private Integer size;

    @Size(min = 1, message = "Name cannot be empty if provided")
    private String sortBy;

    @Size(min = 1, message = "Name cannot be empty if provided")
    private String sortDir;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }
//    @Min(value = 0, message = "Page must be a positive number")
//    @Builder.Default
//    private Integer page = 0;
//
//    @Min(value = 0, message = "Size must be a positive number")
//    @Builder.Default
//    private Integer size = 10;
//
//    @Size(min = 1, message = "Name cannot be empty if provided")
//    @Builder.Default
//    private String sortBy = "created_at";
//
//    @Size(min = 1, message = "Name cannot be empty if provided")
//    @Builder.Default
//    private String sortDir = "asc";
}
