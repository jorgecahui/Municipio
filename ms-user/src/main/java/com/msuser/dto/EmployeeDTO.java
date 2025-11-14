package com.msuser.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private Long authUserId;

    @NotBlank
    private String employeeCode;

    @NotBlank
    @Pattern(regexp = "^[0-9]{8}$")
    private String documentNumber;

    @NotBlank
    @Size(min = 2, max = 100)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 100)
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^[0-9]{9}$")
    private String phoneNumber;

    private String position;

    @NotNull
    private Long areaId;

    private LocalDate hireDate;
    private Boolean active;
    private LocalDateTime createdAt;
}
