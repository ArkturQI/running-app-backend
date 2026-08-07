package com.AppRun.RunningAppBackend.service;

import com.AppRun.RunningAppBackend.dto.WorkoutRequestDto;
import com.AppRun.RunningAppBackend.entity.User;
import com.AppRun.RunningAppBackend.entity.Workout;
import com.AppRun.RunningAppBackend.repository.UserRepository;
import com.AppRun.RunningAppBackend.repository.WorkoutRepository;
import com.AppRun.RunningAppBackend.util.CurrentUserUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public Workout createWorkoutFromDto(WorkoutRequestDto dto) {
        System.out.println("🔍 [Service] Создание тренировки...");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();

        System.out.println("[Service] Email из токена: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("[Service] Пользователь не найден: " + email);
                    return new RuntimeException("Пользователь не найден: " + email);
                });

        System.out.println("[Service] Пользователь найден: ID = " + user.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startTime = LocalDateTime.parse(dto.getStartTime(), formatter);
        LocalDateTime endTime = dto.getEndTime() != null
                ? LocalDateTime.parse(dto.getEndTime(), formatter)
                : LocalDateTime.now();

        Workout workout = new Workout();
        workout.setUser(user);
        workout.setStartTime(startTime);
        workout.setEndTime(endTime);
        workout.setDistanceKm(dto.getDistanceKm());
        workout.setDurationMinutes(dto.getDurationMinutes());
        workout.setCalories(dto.getCalories());

        Workout saved = workoutRepository.save(workout);

        System.out.println("[Service] Тренировка сохранена! ID = " + saved.getId());

        return saved;
    }

    public List<Workout> getMyWorkouts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();

        System.out.println("[Service] Получение тренировок для: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + email));

        List<Workout> workouts = workoutRepository.findByUserId(user.getId());
        System.out.println("[Service] Найдено тренировок: " + workouts.size());

        return workouts;
    }

    public void deleteWorkout(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тренировка не найдена"));

        if (!workout.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Доступ запрещён");
        }

        workoutRepository.delete(workout);
    }
}
