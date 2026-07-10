package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class CourseRequest {

  @NotBlank(message = "Title is required")
  @Size(max = 100, message = "Title must be less than 100 words")
  private String title;

  @NotNull(message = "Start date is required")
  private Instant startDate;

  @NotNull(message = "End date is required")
  private Instant endDate;
}
