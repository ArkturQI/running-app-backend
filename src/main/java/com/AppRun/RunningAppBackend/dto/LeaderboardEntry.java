package com.AppRun.RunningAppBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntry {
    private Long rank;
    private Long userId;
    private String username;
    private String email;
    private Double totalDistance;
    private Integer totalWorkouts;
    private Long totalDuration;
    private Integer totalCalories;
}