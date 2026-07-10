package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Transactional
  public User create(UserRequest user) {
    return userRepository
        .insertIgnoreConflict(
            user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail())
        .orElseThrow(() -> new NotFoundException("User already exists"));
  }

  @Transactional
  public User update(UUID id, UserRequest user) {
    return userRepository
        .update(id, user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail())
        .orElseThrow(() -> new NotFoundException("User with id: " + id + " does not exist"));
  }

  @Transactional
  public void deleteById(UUID id) {
    if (userRepository.findById(id).isEmpty()) {
      throw new BadRequestException("User with id: " + id + " does not exist");
    }
    userRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public User getUserById(UUID id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("User with id: " + id + "not found"));
  }
}
