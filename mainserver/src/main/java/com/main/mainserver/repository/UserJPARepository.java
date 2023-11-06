package com.main.mainserver.repository;

import com.main.mainserver.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJPARepository extends JpaRepository <User, Long> {
    Optional<User> findUserByEmail(String email);
}
