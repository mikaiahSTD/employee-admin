package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.Course;
import com.example.demo.entity.Subscription;
import com.example.demo.entity.User;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SubscriptionService;
import java.util.UUID;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

  private final SubscriptionService subscriptionService;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;

  @PostMapping
  public ResponseEntity<Subscription> createSubscription(
      @RequestParam UUID userId, @RequestParam UUID courseId) {
    User user = userRepository.findById(userId).orElseThrow();
    Course course = courseRepository.findById(courseId).orElseThrow();

    return ResponseEntity.ok(subscriptionService.subscribe(user, course));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSubscription(@PathVariable UUID id) {
    subscriptionService.unsubscribe(id);
    return ResponseEntity.noContent().build();
  }
}
