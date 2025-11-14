package com.msuser.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class CitizenDTO {
    private Long id;
    private Long authUserId;

    @NotBlank
    @Pattern(regexp = "^[0-9]{8}$")
    private String documentNumber;

    @NotBlank
    @Size(min = 2, max = 100)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 100)
    private String lastName;

    @Email
    private String email;

    @Pattern(regexp = "^[0-9]{9}$")
    private String phoneNumber;

    private LocalDate birthDate;
    private String gender;
    private String address;
    private String district;
    private String province;
    private String department;
    private String observations;
    private Boolean active;
    private LocalDateTime createdAt;
}
