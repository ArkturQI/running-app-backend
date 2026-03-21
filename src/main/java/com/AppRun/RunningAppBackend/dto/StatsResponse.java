package com.AppRun.RunningAppBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {
    private Double totalDistance;        // Общая дистанция (км)
    private Long totalDuration;          // Общая длительность (минуты)
    private Integer totalCalories;       // Всего калорий
    private Double averagePace;          // Средний темп (мин/км)
    private Integer workoutCount;        // Количество тренировок
    private Double bestDistance;         // Лучшая дистанция за раз
    private Long bestDuration;           // Лучшая длительность за раз
    private LocalDateTime periodStart;   // Начало периода
    private LocalDateTime periodEnd;     // Конец периода
    private Double distanceChange;       // Изменение дистанции (%) для сравнения
    private Double durationChange;       // Изменение времени (%) для сравнения
    private Double caloriesChange;       // Изменение калорий (%) для сравнения
}