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

  @NotBlank
  @Size(max = 100)
  private String lastName;

  @Size(max = 100)
  private String firstName;

  @Size(max = 100)
  private String userName;

  @NotBlank
  @Email
  @Size(max = 100)
  private String email;
}
