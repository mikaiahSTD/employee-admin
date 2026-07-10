package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.CourseRequest;
import com.example.demo.service.CourseService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/courses")
@RequiredArgsConstructor
@RestController
public class CourseController {
  private final CourseService courseService;

  @GetMapping
  public ResponseEntity<?> getAllCourses() {
    return ResponseEntity.status(HttpStatus.OK).body(courseService.getAll());
  }

  @GetMapping("/{courseId}")
  public ResponseEntity<?> getCourse(@PathVariable UUID courseId) {
    return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourseById(courseId));
  }

  @PostMapping
  public ResponseEntity<?> createCourse(@RequestBody CourseRequest course) {
    return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(course));
  }

  @PutMapping("/{courseId}")
  public ResponseEntity<?> updateCourse(
      @PathVariable UUID courseId, @RequestBody CourseRequest course) {
    return ResponseEntity.status(HttpStatus.OK).body(courseService.update(courseId, course));
  }

  @DeleteMapping("/{courseId}")
  public ResponseEntity<?> deleteCourse(@PathVariable UUID courseId) {
    courseService.deleteById(courseId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
