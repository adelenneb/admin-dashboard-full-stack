package com.example.adminapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
  @NotBlank String username,
  @Email @NotBlank String email,
  String password,
  @NotBlank String role
) {
}
