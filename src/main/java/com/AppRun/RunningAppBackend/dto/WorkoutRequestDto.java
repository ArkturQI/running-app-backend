package com.AppRun.RunningAppBackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutRequestDto {

    @NotNull(message = "ID пользователя обязателен")
    private Long userId;

    @NotNull(message = "Время начала обязательно")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Positive(message = "Дистанция должна быть больше 0")
    private Double distanceKm;

    private Integer durationMinutes;
    private Integer calories;
}