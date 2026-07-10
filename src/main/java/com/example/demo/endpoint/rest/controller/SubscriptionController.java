package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.SubscriptionRequest;
import com.example.demo.endpoint.event.EventProducer;
import com.example.demo.endpoint.event.model.SendEmailSubscriptionValidated;
import com.example.demo.entity.Course;
import com.example.demo.entity.User;
import com.example.demo.service.CourseService;
import com.example.demo.service.SubscriptionService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

  private final SubscriptionService subscriptionService;
  private final UserService userService;
  private final CourseService courseService;
  private final EventProducer<SendEmailSubscriptionValidated> eventProducer;

  @PostMapping
  @SneakyThrows
  public ResponseEntity<?> createSubscription(@Valid @RequestBody SubscriptionRequest request) {
    User user = userService.getUserById(request.getUserId());
    Course course = courseService.getCourseById(request.getCourseId());
    var event =
        SendEmailSubscriptionValidated.builder()
            .courseId(course.getId())
            .to(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .courseTitle(course.getTitle())
            .courseStartDate(course.getStartDate())
            .courseEndDate(course.getEndDate())
            .build();
    eventProducer.accept(List.of(event));
    return ResponseEntity.ok(subscriptionService.subscribe(user, course));
  }

  @DeleteMapping("/{subscriptionId}")
  public ResponseEntity<Void> deleteSubscription(@PathVariable UUID subscriptionId) {
    subscriptionService.unsubscribe(subscriptionId);
    return ResponseEntity.noContent().build();
  }
}
