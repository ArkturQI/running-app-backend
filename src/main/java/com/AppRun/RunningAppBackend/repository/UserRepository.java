package com.AppRun.RunningAppBackend.repository;

import com.AppRun.RunningAppBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Найти пользователя по email
    Optional<User> findByEmail(String email);

    // Проверить существует ли email (для валидации)
    boolean existsByEmail(String email);

    // Найти по Friend Code
    @Query("SELECT u FROM User u WHERE u.friendCode = :code")
    Optional<User> findByFriendCode(@Param("code") String code);
}