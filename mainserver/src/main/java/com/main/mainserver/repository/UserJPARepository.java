package com.main.mainserver.repository;

import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserJPARepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    boolean existsByEmailOrUsername(String email, String username);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.userStatus = :userStatus WHERE u.id = :userId")
    int banUser(UserStatus userStatus, Long userId);

}
