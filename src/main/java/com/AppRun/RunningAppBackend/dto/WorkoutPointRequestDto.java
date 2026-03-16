package com.AppRun.RunningAppBackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPointRequestDto {

    @NotNull(message = "ID тренировки обязателен")
    private Long workoutId;

    @NotNull(message = "Широта обязательна")
    private Double latitude;

    @NotNull(message = "Долгота обязательна")
    private Double longitude;

    @NotNull(message = "Время обязательно")
    private LocalDateTime timestamp;

    private Double altitude;
    private Float accuracy;
}