package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
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
  @NotNull(message = "User id is required")
  private UUID userId;

  @NotNull(message = "Course id is required")
  private UUID courseId;
}
