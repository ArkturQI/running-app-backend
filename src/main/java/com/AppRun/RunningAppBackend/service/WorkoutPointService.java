package com.AppRun.RunningAppBackend.service;

import com.AppRun.RunningAppBackend.dto.WorkoutPointRequestDto;
import com.AppRun.RunningAppBackend.dto.WorkoutPointResponseDto;
import com.AppRun.RunningAppBackend.entity.Workout;
import com.AppRun.RunningAppBackend.entity.WorkoutPoint;
import com.AppRun.RunningAppBackend.repository.WorkoutPointRepository;
import com.AppRun.RunningAppBackend.repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutPointService {

    private final WorkoutPointRepository pointRepository;
    private final WorkoutRepository workoutRepository;

    public WorkoutPointService(WorkoutPointRepository pointRepository, WorkoutRepository workoutRepository) {
        this.pointRepository = pointRepository;
        this.workoutRepository = workoutRepository;
    }

    // Создать одну точку
    public WorkoutPoint createPoint(WorkoutPointRequestDto dto) {
        Workout workout = workoutRepository.findById(dto.getWorkoutId())
                .orElseThrow(() -> new RuntimeException("Тренировка не найдена с ID: " + dto.getWorkoutId()));

        WorkoutPoint point = new WorkoutPoint();
        point.setWorkout(workout);
        point.setLatitude(dto.getLatitude());
        point.setLongitude(dto.getLongitude());
        point.setTimestamp(dto.getTimestamp());
        point.setAltitude(dto.getAltitude());
        point.setAccuracy(dto.getAccuracy());

        return pointRepository.save(point);
    }

    // Получить все точки тренировки
    public List<WorkoutPointResponseDto> getPointsByWorkoutId(Long workoutId) {
        List<WorkoutPoint> points = pointRepository.findByWorkoutId(workoutId);
        return points.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Конвертация Entity → DTO
    private WorkoutPointResponseDto convertToDto(WorkoutPoint point) {
        WorkoutPointResponseDto dto = new WorkoutPointResponseDto();
        dto.setId(point.getId());
        dto.setWorkoutId(point.getWorkout().getId());
        dto.setLatitude(point.getLatitude());
        dto.setLongitude(point.getLongitude());
        dto.setTimestamp(point.getTimestamp());
        dto.setAltitude(point.getAltitude());
        dto.setAccuracy(point.getAccuracy());
        return dto;
    }
}