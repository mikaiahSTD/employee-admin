package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.Course;
import com.example.demo.entity.Subscription;
import com.example.demo.entity.User;
import com.example.demo.service.CourseService;
import com.example.demo.service.SubscriptionService;
import com.example.demo.service.UserService;
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

  @PostMapping
  public ResponseEntity<Subscription> createSubscription(
      @RequestParam UUID userId, @RequestParam UUID courseId) {
    User user = userService.getUserById(userId);
    Course course = courseService.getCourseById(courseId);

    return ResponseEntity.ok(subscriptionService.subscribe(user, course));
  }

  @DeleteMapping("/{subscriptionId}")
  public ResponseEntity<Void> deleteSubscription(@PathVariable UUID subscriptionId) {
    subscriptionService.unsubscribe(subscriptionId);
    return ResponseEntity.noContent().build();
  }
}
