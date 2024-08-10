package com.example.ViChat.repository;

import com.example.ViChat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u from User u WHERE u.fullName LIKE %:query% or u.email LIKE %:query%")
    List<User> searchUser(@Param("query") String query);
}
