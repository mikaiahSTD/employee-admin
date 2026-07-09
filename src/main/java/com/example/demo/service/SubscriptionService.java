package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.Subscription;
import com.example.demo.entity.User;
import com.example.demo.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public Subscription subscribe(User user, Course course) {
        if (subscriptionRepository.existsByUserAndCourse(user, course)) {
            throw new IllegalStateException("L'utilisateur est déjà inscrit à ce cours.");
        }

        Subscription sub = Subscription.builder()
                .user(user)
                .course(course)
                .subscribedAt(Instant.now())
                .build();

        return subscriptionRepository.save(sub);
    }

    @Transactional
    public void unsubscribe(UUID subscriptionId) {
        subscriptionRepository.deleteById(subscriptionId);
    }
}
