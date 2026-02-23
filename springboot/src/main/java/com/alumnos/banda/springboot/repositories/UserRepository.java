package com.alumnos.banda.springboot.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.alumnos.banda.springboot.models.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
}