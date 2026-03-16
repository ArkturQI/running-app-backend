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

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<Workout> createWorkout(@RequestBody @Valid WorkoutRequestDto dto) {
        Workout created = workoutService.createWorkoutFromDto(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/my")  // ← Изменённый путь
    public ResponseEntity<List<WorkoutResponseDto>> getMyWorkouts() {
        List<WorkoutResponseDto> workouts = workoutService.getMyWorkouts();
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponseDto> getWorkoutById(@PathVariable Long id) {
        WorkoutResponseDto workout = workoutService.getWorkoutById(id);
        return ResponseEntity.ok(workout);
    }
}