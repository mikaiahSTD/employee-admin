package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class SubscriptionRequest {
  @NotBlank(message = "User id is required")
  private UUID userId;

  @NotBlank(message = "Course is is required")
  private UUID courseId;
}
