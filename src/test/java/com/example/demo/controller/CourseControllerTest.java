package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.CourseRequest;
import com.example.demo.endpoint.rest.controller.CourseController;
import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private CourseService courseService;

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
  void shouldGetAllCourses() throws Exception {

    when(courseService.getAll()).thenReturn(List.of(course));

    mockMvc
        .perform(get("/api/courses"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Java"));
  }

  @Test
  void shouldGetCourseById() throws Exception {

    when(courseService.getCourseById(courseId)).thenReturn(course);

    mockMvc
        .perform(get("/api/courses/{id}", courseId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Java"));
  }

  @Test
  void shouldCreateCourse() throws Exception {

    when(courseService.create(any())).thenReturn(course);

    mockMvc
        .perform(
            post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Java"));
  }

  @Test
  void shouldUpdateCourse() throws Exception {

    when(courseService.update(eq(courseId), any())).thenReturn(course);

    mockMvc
        .perform(
            put("/api/courses/{id}", courseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Java"));
  }

  @Test
  void shouldDeleteCourse() throws Exception {

    doNothing().when(courseService).deleteById(courseId);

    mockMvc.perform(delete("/api/courses/{id}", courseId)).andExpect(status().isNoContent());

    verify(courseService).deleteById(courseId);
  }
}
