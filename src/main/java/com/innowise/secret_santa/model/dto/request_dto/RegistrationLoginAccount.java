package com.innowise.secret_santa.model.dto.request_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationLoginAccount {

    @Email(message = "Email is invalid")
    @NotBlank(message = "Email field must not be empty")
    private String email;

    @NotBlank(message = "Password field must not be empty")
    @Size(min = 5, message = "Min size password is 5")
    private String password;
}