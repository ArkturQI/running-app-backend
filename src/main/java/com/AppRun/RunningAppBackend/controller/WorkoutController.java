package com.AppRun.RunningAppBackend.controller;

import com.AppRun.RunningAppBackend.dto.WorkoutRequestDto;
import com.AppRun.RunningAppBackend.entity.Workout;
import com.AppRun.RunningAppBackend.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@CrossOrigin(origins = "*")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/my")
    public ResponseEntity<List<Workout>> getMyWorkouts() {
        System.out.println("🔍 [Controller] GET /api/workouts/my");

        try {
            List<Workout> workouts = workoutService.getMyWorkouts();
            return ResponseEntity.ok(workouts);
        } catch (Exception e) {
            System.out.println("❌ [Controller] Ошибка: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Workout> createWorkout(@RequestBody @Valid WorkoutRequestDto dto) {
        System.out.println("🔍 [Controller] POST /api/workouts");

        try {
            Workout workout = workoutService.createWorkoutFromDto(dto);
            return ResponseEntity.ok(workout);
        } catch (Exception e) {
            System.out.println("❌ [Controller] Ошибка: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        System.out.println("🔍 [Controller] DELETE /api/workouts/" + id);

        try {
            workoutService.deleteWorkout(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("❌ [Controller] Ошибка: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}