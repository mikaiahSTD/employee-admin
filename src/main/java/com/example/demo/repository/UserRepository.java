package com.example.demo.repository;

import com.example.demo.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  @Query(
      value =
          """
          INSERT INTO users (id, first_name,last_name,username,email)
          VALUES (gen_random_uuid(), :firstName,:lastName,:username,:email)
          ON CONFLICT (email) DO NOTHING
          returning id, first_name,last_name,username,email
          """,
      nativeQuery = true)
  Optional<User> insertIgnoreConflict(
      @Param("firstName") String firstName,
      @Param("lastName") String lastName,
      @Param("username") String username,
      @Param("email") String email);

  @Query(
      value =
          """
          UPDATE users
          SET first_name = :firstName,
                                  last_name = :lastName,
                                             username = :username,
                                                           email = :email
          WHERE id = :id
          RETURNING id, first_name ,last_name,username,email
          """,
      nativeQuery = true)
  Optional<User> update(
      @Param("id") UUID id,
      @Param("firstName") String firstName,
      @Param("lastName") String lastName,
      @Param("username") String username,
      @Param("email") String email);
}
