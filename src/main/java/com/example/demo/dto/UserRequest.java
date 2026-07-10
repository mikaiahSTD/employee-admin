package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserRequest {

  @Size(max = 100)
  private String lastName;

  @NotBlank(message = "First name is required")
  @Size(max = 100, message = "First name must be less than 100 words")
  private String firstName;

  @NotBlank(message = "Username is required")
  @Size(max = 100, message = "Username must be less than 100 words")
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Malformed email")
  @Size(max = 100, message = "Email must be less than 100 words")
  private String email;
}
