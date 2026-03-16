package com.AppRun.RunningAppBackend.controller;

import com.AppRun.RunningAppBackend.dto.WorkoutPointRequestDto;
import com.AppRun.RunningAppBackend.dto.WorkoutPointResponseDto;
import com.AppRun.RunningAppBackend.entity.WorkoutPoint;
import com.AppRun.RunningAppBackend.service.WorkoutPointService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-points")
public class WorkoutPointController {

    private final WorkoutPointService pointService;

    public WorkoutPointController(WorkoutPointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping
    public ResponseEntity<WorkoutPoint> createPoint(@RequestBody @Valid WorkoutPointRequestDto dto) {
        WorkoutPoint created = pointService.createPoint(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/workout/{workoutId}")
    public ResponseEntity<List<WorkoutPointResponseDto>> getPointsByWorkout(@PathVariable Long workoutId) {
        List<WorkoutPointResponseDto> points = pointService.getPointsByWorkoutId(workoutId);
        return ResponseEntity.ok(points);
    }
}