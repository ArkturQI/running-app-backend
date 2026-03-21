package com.AppRun.RunningAppBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "personal_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double distanceKm;  // 0.4, 1, 2, 1.609 (миля), 5, 10, 21.1, 42.2

    @Column(nullable = false)
    private String distanceName;  // "400м", "1км", "Миля", "5К" и т.д.

    @Column(nullable = false)
    private Long bestTimeSeconds;  // Лучшее время в секундах

    @Column
    private LocalDateTime achievedAt;  // Когда установлен рекорд

    @Column
    private Long workoutId;  // ID тренировки где установлен

    @Column
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime updatedAt;
}