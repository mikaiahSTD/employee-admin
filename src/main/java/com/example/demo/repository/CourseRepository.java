package com.example.demo.repository;

import com.example.demo.entity.Course;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
  @Query(
      value =
          """
          INSERT INTO users (id, title,startDate,endDate)
          VALUES (gen_random_uuid(), :title,:startDate,:endDate)
          ON CONFLICT (name) DO NOTHING
          returning id, title,startDate,endDate
          """,
      nativeQuery = true)
  Optional<Course> insertIgnoreConflict(
      @Param("title") String title,
      @Param("startDate") Instant startDate,
      @Param("endDate") Instant endDate);

  @Query(
      value =
          """
          UPDATE genre
          SET title = :title,
                                  startDate = :startDate,
                                             endDate = :endDate,
          WHERE id = :id
          RETURNING id, title,startDate,endDate
          """,
      nativeQuery = true)
  Optional<Course> update(
      @Param("id") UUID id,
      @Param("title") String title,
      @Param("startDate") Instant startDate,
      @Param("endDate") Instant endDate);
}
