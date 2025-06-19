package com.ecommerce.totolo.repository;

import com.ecommerce.totolo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IUserDao extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);        // Para login
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);             // Para validar si ya existe
    boolean existsByEmail(String email);
}
