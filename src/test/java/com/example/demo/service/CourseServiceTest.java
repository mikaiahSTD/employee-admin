package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.example.demo.dto.CourseRequest;
import com.example.demo.entity.Course;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.CourseRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

  @Mock private CourseRepository courseRepository;

  @InjectMocks private CourseService courseService;

  private UUID courseId;
  private Course course;
  private CourseRequest request;

  @BeforeEach
  void setUp() {
    courseId = UUID.randomUUID();

    course =
        Course.builder()
            .id(courseId)
            .title("Java")
            .startDate(Instant.now())
            .endDate(Instant.now().plusSeconds(3600))
            .build();

    request = new CourseRequest("Java", course.getStartDate(), course.getEndDate());
  }

  @Test
  void shouldGetCourseById() {
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

    Course result = courseService.getCourseById(courseId);

    assertEquals(course, result);
    verify(courseRepository).findById(courseId);
  }

  @Test
  void shouldThrowWhenCourseNotFound() {
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> courseService.getCourseById(courseId));

    verify(courseRepository).findById(courseId);
  }

  @Test
  void shouldReturnAllCourses() {

    List<Course> courses = List.of(course);

    when(courseRepository.findAll()).thenReturn(courses);

    List<Course> result = courseService.getAll();

    assertEquals(1, result.size());
    verify(courseRepository).findAll();
  }

  @Test
  void shouldCreateCourse() {

    when(courseRepository.insertIgnoreConflict(
            request.getTitle(), request.getStartDate(), request.getEndDate()))
        .thenReturn(Optional.of(course));

    Course result = courseService.create(request);

    assertEquals(course, result);
  }

  @Test
  void shouldThrowWhenCourseAlreadyExists() {

    when(courseRepository.insertIgnoreConflict(any(), any(), any())).thenReturn(Optional.empty());

    assertThrows(ConflictException.class, () -> courseService.create(request));
  }

  @Test
  void shouldUpdateCourse() {

    when(courseRepository.update(eq(courseId), any(), any(), any()))
        .thenReturn(Optional.of(course));

    Course result = courseService.update(courseId, request);

    assertEquals(course, result);
  }

  @Test
  void shouldThrowWhenUpdateFails() {

    when(courseRepository.update(eq(courseId), any(), any(), any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> courseService.update(courseId, request));
  }

  @Test
  void shouldDeleteCourse() {

    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

    doNothing().when(courseRepository).deleteById(courseId);

    courseService.deleteById(courseId);

    verify(courseRepository).deleteById(courseId);
  }

  @Test
  void shouldThrowWhenDeletingUnknownCourse() {

    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> courseService.deleteById(courseId));

    verify(courseRepository, never()).deleteById(any());
  }
}
