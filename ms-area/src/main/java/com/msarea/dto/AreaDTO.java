package com.msarea.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaDTO {
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String code;

    private String description;
    private Long parentAreaId;
    private Long supervisorId;
    private String location;

    @Pattern(regexp = "^[0-9]{7,9}$")
    private String phone;

    @Email
    private String email;

    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
