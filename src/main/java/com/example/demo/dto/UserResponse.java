package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserResponse {

  @NotNull private UUID id;

  @NotBlank
  @Size(max = 100)
  private String lastName;

  @Size(max = 100)
  private String firstName;

  @Size(max = 100)
  private String username;

  @NotBlank
  @Email
  @Size(max = 100)
  private String email;
}
