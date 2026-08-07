package com.AppRun.RunningAppBackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "workouts")
@Data
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Время начала обязательно")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Min(value = 0, message = "Дистанция не может быть отрицательной")
    @Column(name = "distance_km", nullable = false)
    private Double distanceKm;

    @Min(value = 0, message = "Длительность не может быть отрицательной")
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Min(value = 0, message = "Калории не могут быть отрицательными")
    @Column(name = "calories")
    private Integer calories;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
