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
          INSERT INTO users (id, firstName,lastName,username,email)
          VALUES (gen_random_uuid(), :name)
          ON CONFLICT (name) DO NOTHING
          returning id, firstName,lastName,username,email
          """,
      nativeQuery = true)
  Optional<User> insertIgnoreConflict(
      @Param("firtName") String firstName,
      @Param("lastName") String lastName,
      @Param("username") String username,
      @Param("email") String email);

  @Query(
      value =
          """
          UPDATE genre
          SET firstName = :firstName,
                                  lastName = :lastName,
                                             username = :username,
                                                           email = :email
          WHERE id = :id
          RETURNING id, firstName ,lastName,username,email
          """,
      nativeQuery = true)
  Optional<User> update(
      @Param("id") UUID id,
      @Param("firtName") String firstName,
      @Param("lastName") String lastName,
      @Param("username") String username,
      @Param("email") String email);
}
