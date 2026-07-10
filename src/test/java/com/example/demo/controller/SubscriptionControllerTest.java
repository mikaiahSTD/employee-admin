package com.example.demo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.endpoint.event.EventProducer;
import com.example.demo.endpoint.event.model.SendEmailSubscriptionValidated;
import com.example.demo.endpoint.rest.controller.SubscriptionController;
import com.example.demo.entity.Course;
import com.example.demo.entity.Subscription;
import com.example.demo.entity.User;
import com.example.demo.service.CourseService;
import com.example.demo.service.SubscriptionService;
import com.example.demo.service.UserService;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private SubscriptionService subscriptionService;

  @MockBean private UserService userService;

  @MockBean private CourseService courseService;

  @MockBean private EventProducer<SendEmailSubscriptionValidated> eventProducer;

  private User user;
  private Course course;
  private Subscription subscription;
  private UUID userId;
  private UUID courseId;
  private UUID subscriptionId;

  @BeforeEach
  void setUp() {

    userId = UUID.randomUUID();
    courseId = UUID.randomUUID();
    subscriptionId = UUID.randomUUID();

    user = User.builder().id(userId).firstName("John").lastName("Doe").build();

    course = Course.builder().id(courseId).title("Java").build();

    subscription =
        Subscription.builder()
            .id(subscriptionId)
            .user(user)
            .course(course)
            .subscribedAt(Instant.now())
            .build();
  }

  @Test
  void shouldCreateSubscription() throws Exception {

    when(userService.getUserById(userId)).thenReturn(user);

    when(courseService.getCourseById(courseId)).thenReturn(course);

    when(subscriptionService.subscribe(user, course)).thenReturn(subscription);

    mockMvc
        .perform(
            post("/api/subscriptions")
                .param("userId", userId.toString())
                .param("courseId", courseId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(subscriptionId.toString()))
        .andExpect(jsonPath("$.user.firstName").value("John"))
        .andExpect(jsonPath("$.course.title").value("Java"));

    verify(eventProducer).accept(any());
  }

  @Test
  void shouldDeleteSubscription() throws Exception {

    doNothing().when(subscriptionService).unsubscribe(subscriptionId);

    mockMvc
        .perform(delete("/api/subscriptions/{id}", subscriptionId))
        .andExpect(status().isNoContent());

    verify(subscriptionService).unsubscribe(subscriptionId);
  }
}
