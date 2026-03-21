package com.AppRun.RunningAppBackend.service;

import com.AppRun.RunningAppBackend.dto.LeaderboardEntry;
import com.AppRun.RunningAppBackend.entity.User;
import com.AppRun.RunningAppBackend.entity.Workout;
import com.AppRun.RunningAppBackend.repository.UserRepository;
import com.AppRun.RunningAppBackend.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderboardService {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;

    public LeaderboardService(UserRepository userRepository, WorkoutRepository workoutRepository) {
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
    }

    /**
     * Получить лидерборд за период
     * @param period day, week, month, year, all
     * @return Список пользователей с статистикой
     */
    public List<LeaderboardEntry> getLeaderboard(String period) {
        LocalDateTime startDate = getStartDate(period);
        List<User> allUsers = userRepository.findAll();

        List<LeaderboardEntry> leaderboard = new ArrayList<>();

        for (User user : allUsers) {
            // Получить тренировки пользователя за период
            List<Workout> workouts = workoutRepository.findByUserIdAndDateRange(
                    user.getId(), startDate, LocalDateTime.now());

            // Пропустить если нет тренировок
            if (workouts.isEmpty()) {
                continue;
            }

            // Расчёт статистики
            double totalDistance = workouts.stream()
                    .mapToDouble(w -> w.getDistanceKm() != null ? w.getDistanceKm() : 0.0)
                    .sum();

            int totalWorkouts = workouts.size();

            long totalDuration = workouts.stream()
                    .mapToLong(w -> w.getDurationMinutes() != null ? w.getDurationMinutes() : 0)
                    .sum();

            int totalCalories = workouts.stream()
                    .mapToInt(w -> w.getCalories() != null ? w.getCalories() : 0)
                    .sum();

            // Добавить в лидерборд
            leaderboard.add(new LeaderboardEntry(
                    0L,  // Rank установим позже
                    user.getId(),
                    user.getEmail(),  // Username (так как поля username нет)
                    user.getEmail(),
                    Math.round(totalDistance * 100.0) / 100.0,  // Округление до 2 знаков
                    totalWorkouts,
                    totalDuration,
                    totalCalories
            ));
        }

        // Сортировка по дистанции (убывание)
        leaderboard.sort((a, b) -> Double.compare(b.getTotalDistance(), a.getTotalDistance()));

        // Установка рангов
        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboard.get(i).setRank((long) (i + 1));
        }

        return leaderboard;
    }

    /**
     * Получить дату начала периода
     * @param period day, week, month, year, all
     * @return LocalDateTime начала периода
     */
    private LocalDateTime getStartDate(String period) {
        LocalDateTime now = LocalDateTime.now();

        switch (period.toLowerCase()) {
            case "day":
                return now.minusDays(1);
            case "week":
                return now.minusWeeks(1);
            case "month":
                return now.minusMonths(1);
            case "year":
                return now.minusYears(1);
            case "all":
            default:
                return LocalDateTime.of(2000, 1, 1, 0, 0);
        }
    }
}