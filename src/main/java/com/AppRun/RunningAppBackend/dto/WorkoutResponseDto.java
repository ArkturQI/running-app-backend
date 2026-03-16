package com.AppRun.RunningAppBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutResponseDto {

    private Long id;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double distanceKm;
    private Integer durationMinutes;
    private Integer calories;
    private LocalDateTime createdAt;
}