package com.AppRun.RunningAppBackend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkoutRequestDto {

    @NotNull(message = "Время начала обязательно")
    private String startTime;

    private String endTime;

    @Min(value = 0, message = "Дистанция не может быть отрицательной")
    private Double distanceKm;

    @Min(value = 0, message = "Длительность не может быть отрицательной")
    private Integer durationMinutes;

    @Min(value = 0, message = "Калории не могут быть отрицательными")
    private Integer calories;
}