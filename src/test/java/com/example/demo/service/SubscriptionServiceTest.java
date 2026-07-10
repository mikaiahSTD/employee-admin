package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.entity.Course;
import com.example.demo.entity.Subscription;
import com.example.demo.entity.User;
import com.example.demo.repository.SubscriptionRepository;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

  @Mock private SubscriptionRepository subscriptionRepository;

  @InjectMocks private SubscriptionService subscriptionService;

  private User user;
  private Course course;
  private Subscription subscription;
  private UUID subscriptionId;

  @BeforeEach
  void setUp() {

    subscriptionId = UUID.randomUUID();

    user = User.builder().id(UUID.randomUUID()).firstName("John").lastName("Doe").build();

    course = Course.builder().id(UUID.randomUUID()).title("Java").build();

    subscription =
        Subscription.builder()
            .id(subscriptionId)
            .user(user)
            .course(course)
            .subscribedAt(Instant.now())
            .build();
  }

  @Test
  void shouldSubscribeUser() {

    when(subscriptionRepository.existsByUserAndCourse(user, course)).thenReturn(false);

    when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

    Subscription result = subscriptionService.subscribe(user, course);

    assertEquals(subscription, result);

    verify(subscriptionRepository).existsByUserAndCourse(user, course);
    verify(subscriptionRepository).save(any(Subscription.class));
  }

  @Test
  void shouldThrowWhenAlreadySubscribed() {

    when(subscriptionRepository.existsByUserAndCourse(user, course)).thenReturn(true);

    assertThrows(IllegalStateException.class, () -> subscriptionService.subscribe(user, course));

    verify(subscriptionRepository, never()).save(any());
  }

  @Test
  void shouldUnsubscribe() {

    doNothing().when(subscriptionRepository).deleteById(subscriptionId);

    subscriptionService.unsubscribe(subscriptionId);

    verify(subscriptionRepository).deleteById(subscriptionId);
  }
}
