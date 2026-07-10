package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.UserRequest;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
  private final UserService userService;

  @GetMapping
  public ResponseEntity<?> getAllUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> getUser(@PathVariable UUID userId) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userId));
  }

  @PostMapping
  public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest user) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
  }

  @PutMapping("/{userId}")
  public ResponseEntity<?> updateUser(
      @PathVariable UUID userId, @Valid @RequestBody UserRequest user) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, user));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
    userService.deleteById(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
