package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.example.demo.dto.UserRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserService userService;

  private UUID userId;
  private User user;
  private UserRequest request;

  @BeforeEach
  void setUp() {

    userId = UUID.randomUUID();

    user =
        User.builder()
            .id(userId)
            .firstName("John")
            .lastName("Doe")
            .username("jdoe")
            .email("john@hei.school")
            .build();

    request = new UserRequest("John", "Doe", "jdoe", "john@hei.school");
  }

  @Test
  void shouldReturnAllUsers() {

    when(userRepository.findAll()).thenReturn(List.of(user));

    List<User> users = userService.findAll();

    assertEquals(1, users.size());
    verify(userRepository).findAll();
  }

  @Test
  void shouldReturnUserById() {

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    User result = userService.getUserById(userId);

    assertEquals(user, result);
    verify(userRepository).findById(userId);
  }

  @Test
  void shouldThrowWhenUserNotFound() {

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.getUserById(userId));

    verify(userRepository).findById(userId);
  }

  @Test
  void shouldCreateUser() {

    when(userRepository.insertIgnoreConflict(
            request.getFirstName(),
            request.getLastName(),
            request.getUserName(),
            request.getEmail()))
        .thenReturn(Optional.of(user));

    User result = userService.create(request);

    assertEquals(user, result);
  }

  @Test
  void shouldThrowWhenCreatingExistingUser() {

    when(userRepository.insertIgnoreConflict(any(), any(), any(), any()))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.create(request));
  }

  @Test
  void shouldUpdateUser() {

    when(userRepository.update(eq(userId), any(), any(), any(), any()))
        .thenReturn(Optional.of(user));

    User result = userService.update(userId, request);

    assertEquals(user, result);
  }

  @Test
  void shouldThrowWhenUpdatingUnknownUser() {

    when(userRepository.update(eq(userId), any(), any(), any(), any()))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.update(userId, request));
  }

  @Test
  void shouldDeleteUser() {

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    doNothing().when(userRepository).deleteById(userId);

    userService.deleteById(userId);

    verify(userRepository).deleteById(userId);
  }

  @Test
  void shouldThrowWhenDeletingUnknownUser() {

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(BadRequestException.class, () -> userService.deleteById(userId));

    verify(userRepository, never()).deleteById(any());
  }
}
