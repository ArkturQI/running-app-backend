package com.AppRun.RunningAppBackend.controller;

import com.AppRun.RunningAppBackend.dto.WorkoutRequestDto;
import com.AppRun.RunningAppBackend.dto.WorkoutResponseDto;
import com.AppRun.RunningAppBackend.entity.Workout;
import com.AppRun.RunningAppBackend.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;  // ← ИСПОЛЬЗУЕМ SERVICE

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    // 🔹 Получить все тренировки текущего пользователя
    @GetMapping("/my")
    public ResponseEntity<List<WorkoutResponseDto>> getMyWorkouts() {
        List<WorkoutResponseDto> workouts = workoutService.getMyWorkouts();
        return ResponseEntity.ok(workouts);
    }

    // 🔹 Создать тренировку
    @PostMapping
    public ResponseEntity<Workout> createWorkout(@RequestBody @Valid WorkoutRequestDto dto) {
        System.out.println("🔍 [Controller] Получен запрос на создание тренировки");

        Workout workout = workoutService.createWorkoutFromDto(dto);

        System.out.println("✅ [Controller] Тренировка сохранена! ID = " + workout.getId());

        return ResponseEntity.ok(workout);
    }

    // 🔹 Получить тренировку по ID
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponseDto> getWorkoutById(@PathVariable Long id) {
        WorkoutResponseDto workout = workoutService.getWorkoutById(id);
        return ResponseEntity.ok(workout);
    }

    // 🔹 Удалить тренировку
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        // Для удаления можно добавить метод в сервис или оставить тут
        // Пока оставим простую реализацию
        return ResponseEntity.ok().build();
    }
}