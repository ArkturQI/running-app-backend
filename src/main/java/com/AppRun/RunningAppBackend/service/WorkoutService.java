package com.AppRun.RunningAppBackend.service;

import com.AppRun.RunningAppBackend.dto.WorkoutRequestDto;
import com.AppRun.RunningAppBackend.dto.WorkoutResponseDto;
import com.AppRun.RunningAppBackend.entity.User;
import com.AppRun.RunningAppBackend.entity.Workout;
import com.AppRun.RunningAppBackend.repository.UserRepository;
import com.AppRun.RunningAppBackend.repository.WorkoutRepository;
import com.AppRun.RunningAppBackend.util.CurrentUserUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final CurrentUserUtil currentUserUtil;

    public WorkoutService(WorkoutRepository workoutRepository,
                          UserRepository userRepository,
                          CurrentUserUtil currentUserUtil) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
        this.currentUserUtil = currentUserUtil;
    }

    // 🔹 Создать тренировку (ИСПРАВЛЕНО: конвертация String → LocalDateTime)
    public Workout createWorkoutFromDto(WorkoutRequestDto dto) {
        Long currentUserId = currentUserUtil.getCurrentUserId();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + currentUserId));

        // 🔹 Конвертация String → LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startTime = LocalDateTime.parse(dto.getStartTime(), formatter);
        LocalDateTime endTime = dto.getEndTime() != null
                ? LocalDateTime.parse(dto.getEndTime(), formatter)
                : LocalDateTime.now();

        Workout workout = new Workout();
        workout.setUser(user);
        workout.setStartTime(startTime);  // ← Теперь LocalDateTime
        workout.setEndTime(endTime);      // ← Теперь LocalDateTime
        workout.setDistanceKm(dto.getDistanceKm());
        workout.setDurationMinutes(dto.getDurationMinutes());
        workout.setCalories(dto.getCalories());

        return workoutRepository.save(workout);
    }

    public List<WorkoutResponseDto> getMyWorkouts() {
        Long currentUserId = currentUserUtil.getCurrentUserId();
        List<Workout> workouts = workoutRepository.findByUserId(currentUserId);
        return workouts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public WorkoutResponseDto getWorkoutById(Long id) {
        Long currentUserId = currentUserUtil.getCurrentUserId();

        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тренировка не найдена"));

        if (!workout.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("Доступ запрещён: это не ваша тренировка");
        }

        return convertToDto(workout);
    }

    private WorkoutResponseDto convertToDto(Workout workout) {
        WorkoutResponseDto dto = new WorkoutResponseDto();
        dto.setId(workout.getId());
        dto.setUserId(workout.getUser().getId());
        dto.setStartTime(workout.getStartTime());
        dto.setEndTime(workout.getEndTime());
        dto.setDistanceKm(workout.getDistanceKm());
        dto.setDurationMinutes(workout.getDurationMinutes());
        dto.setCalories(workout.getCalories());
        dto.setCreatedAt(workout.getCreatedAt());
        return dto;
    }
}