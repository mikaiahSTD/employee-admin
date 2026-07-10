package com.example.demo.service;

import com.example.demo.dto.CourseRequest;
import com.example.demo.entity.Course;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.CourseRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {
  private CourseRepository courseRepository;

  public CourseService(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  @Transactional(readOnly = true)
  public Course getCourseById(UUID id) {
    return courseRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Course with id: " + id + "not found"));
  }

  @Transactional(readOnly = true)
  public List<Course> getAll() {
    return courseRepository.findAll();
  }

  @Transactional
  public Course create(CourseRequest course) {
    return courseRepository
        .insertIgnoreConflict(course.getTitle(), course.getStartDate(), course.getEndDate())
        .orElseThrow(() -> new NotFoundException("User already exists"));
  }

  @Transactional
  public Course update(UUID id, CourseRequest course) {
    return courseRepository
        .update(id, course.getTitle(), course.getStartDate(), course.getEndDate())
        .orElseThrow(() -> new NotFoundException("User with id: " + id + " does not exist"));
  }

  @Transactional
  public void deleteById(UUID id) {
    if (courseRepository.findById(id).isEmpty()) {
      throw new BadRequestException("Course with id: " + id + " does not exist");
    }
    courseRepository.deleteById(id);
  }
}
