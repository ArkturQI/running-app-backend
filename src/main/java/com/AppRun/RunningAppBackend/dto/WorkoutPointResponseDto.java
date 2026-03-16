package com.AppRun.RunningAppBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPointResponseDto {

    private Long id;
    private Long workoutId;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
    private Double altitude;
    private Float accuracy;
}