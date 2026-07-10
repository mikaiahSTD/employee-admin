package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.UserRequest;
import com.example.demo.endpoint.rest.controller.UserController;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private UserService userService;

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
  void shouldGetAllUsers() throws Exception {

    when(userService.findAll()).thenReturn(List.of(user));

    mockMvc
        .perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].firstName").value("John"))
        .andExpect(jsonPath("$[0].lastName").value("Doe"));
  }

  @Test
  void shouldGetUserById() throws Exception {

    when(userService.getUserById(userId)).thenReturn(user);

    mockMvc
        .perform(get("/api/users/{id}", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"));
  }

  @Test
  void shouldCreateUser() throws Exception {

    when(userService.create(any(UserRequest.class))).thenReturn(user);

    mockMvc
        .perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.email").value("john@hei.school"));
  }

  @Test
  void shouldUpdateUser() throws Exception {

    when(userService.update(eq(userId), any(UserRequest.class))).thenReturn(user);

    mockMvc
        .perform(
            put("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("John"));
  }

  @Test
  void shouldDeleteUser() throws Exception {

    doNothing().when(userService).deleteById(userId);

    mockMvc.perform(delete("/api/users/{id}", userId)).andExpect(status().isNoContent());

    verify(userService).deleteById(userId);
  }
}
