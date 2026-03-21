package com.AppRun.RunningAppBackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email не может быть пустым")
    @Size(min = 5, max = 50, message = "Email должен быть от 5 до 50 символов")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    @Column(nullable = false)
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ===== НОВОЕ ПОЛЕ: Friend Code =====
    @Column(name = "friend_code", unique = true, length = 12)
    private String friendCode;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}