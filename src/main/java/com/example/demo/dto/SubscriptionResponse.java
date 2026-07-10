package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class SubscriptionResponse {

  @NotNull private UUID id;

  @NotNull @Valid private UserResponse user;

  @NotNull @Valid private CourseResponse course;

  @NotNull private Instant subscribedAt;
}
