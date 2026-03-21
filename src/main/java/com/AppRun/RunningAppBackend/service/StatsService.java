package com.AppRun.RunningAppBackend.service;

import com.AppRun.RunningAppBackend.dto.StatsResponse;
import com.AppRun.RunningAppBackend.entity.User;
import com.AppRun.RunningAppBackend.entity.Workout;
import com.AppRun.RunningAppBackend.repository.UserRepository;
import com.AppRun.RunningAppBackend.repository.WorkoutRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class StatsService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    public StatsService(WorkoutRepository workoutRepository, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + email));
    }

    public StatsResponse getWeeklyStats(Integer weekNumber, Integer year) {
        User user = getCurrentUser();

        // ← ПРОЩЕ: Берём текущую неделю от понедельника
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        LocalDateTime periodStart = startOfWeek.atStartOfDay();
        LocalDateTime periodEnd = endOfWeek.atTime(23, 59, 59);

        List<Workout> workouts = workoutRepository.findByUserIdAndDateRange(
                user.getId(), periodStart, periodEnd);

        return calculateStats(workouts, periodStart, periodEnd);
    }

    public StatsResponse getMonthlyStats(Integer month, Integer year) {
        User user = getCurrentUser();

        if (month == null) month = LocalDateTime.now().getMonthValue();
        if (year == null) year = Year.now().getValue();

        LocalDateTime periodStart = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime periodEnd = periodStart.plusMonths(1).minusSeconds(1);

        List<Workout> workouts = workoutRepository.findByUserIdAndDateRange(
                user.getId(), periodStart, periodEnd);

        return calculateStats(workouts, periodStart, periodEnd);
    }

    public StatsResponse getYearlyStats(Integer year) {
        User user = getCurrentUser();

        if (year == null) year = Year.now().getValue();

        LocalDateTime periodStart = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime periodEnd = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        List<Workout> workouts = workoutRepository.findByUserIdAndDateRange(
                user.getId(), periodStart, periodEnd);

        return calculateStats(workouts, periodStart, periodEnd);
    }

    public StatsResponse comparePeriods(LocalDateTime start1, LocalDateTime end1,
                                        LocalDateTime start2, LocalDateTime end2) {
        User user = getCurrentUser();

        List<Workout> period1Workouts = workoutRepository.findByUserIdAndDateRange(
                user.getId(), start1, end1);
        List<Workout> period2Workouts = workoutRepository.findByUserIdAndDateRange(
                user.getId(), start2, end2);

        StatsResponse period1Stats = calculateStats(period1Workouts, start1, end1);
        StatsResponse period2Stats = calculateStats(period2Workouts, start2, end2);

        StatsResponse comparison = new StatsResponse();
        comparison.setTotalDistance(period1Stats.getTotalDistance());
        comparison.setTotalDuration(period1Stats.getTotalDuration());
        comparison.setTotalCalories(period1Stats.getTotalCalories());
        comparison.setAveragePace(period1Stats.getAveragePace());
        comparison.setWorkoutCount(period1Stats.getWorkoutCount());
        comparison.setBestDistance(period1Stats.getBestDistance());
        comparison.setBestDuration(period1Stats.getBestDuration());
        comparison.setPeriodStart(start1);
        comparison.setPeriodEnd(end1);

        comparison.setDistanceChange(calculateChange(period2Stats.getTotalDistance(), period1Stats.getTotalDistance()));
        comparison.setDurationChange(calculateChange(period2Stats.getTotalDuration().doubleValue(), period1Stats.getTotalDuration().doubleValue()));
        comparison.setCaloriesChange(calculateChange(period2Stats.getTotalCalories().doubleValue(), period1Stats.getTotalCalories().doubleValue()));

        return comparison;
    }

    private StatsResponse calculateStats(List<Workout> workouts, LocalDateTime periodStart, LocalDateTime periodEnd) {
        StatsResponse stats = new StatsResponse();
        stats.setPeriodStart(periodStart);
        stats.setPeriodEnd(periodEnd);
        stats.setWorkoutCount(workouts.size());

        if (workouts.isEmpty()) {
            stats.setTotalDistance(0.0);
            stats.setTotalDuration(0L);
            stats.setTotalCalories(0);
            stats.setAveragePace(0.0);
            stats.setBestDistance(0.0);
            stats.setBestDuration(0L);
            return stats;
        }

        double totalDistance = workouts.stream()
                .mapToDouble(w -> w.getDistanceKm() != null ? w.getDistanceKm() : 0.0)
                .sum();
        stats.setTotalDistance(Math.round(totalDistance * 100.0) / 100.0);

        long totalDuration = workouts.stream()
                .mapToLong(w -> w.getDurationMinutes() != null ? w.getDurationMinutes() : 0)
                .sum();
        stats.setTotalDuration(totalDuration);

        int totalCalories = workouts.stream()
                .mapToInt(w -> w.getCalories() != null ? w.getCalories() : 0)
                .sum();
        stats.setTotalCalories(totalCalories);

        if (totalDistance > 0) {
            double avgPace = (double) totalDuration / totalDistance;
            stats.setAveragePace(Math.round(avgPace * 100.0) / 100.0);
        } else {
            stats.setAveragePace(0.0);
        }

        double bestDistance = workouts.stream()
                .mapToDouble(w -> w.getDistanceKm() != null ? w.getDistanceKm() : 0.0)
                .max()
                .orElse(0.0);
        stats.setBestDistance(bestDistance);

        long bestDuration = workouts.stream()
                .mapToLong(w -> w.getDurationMinutes() != null ? w.getDurationMinutes() : 0)
                .max()
                .orElse(0L);
        stats.setBestDuration(bestDuration);

        return stats;
    }

    private Double calculateChange(Double previous, Double current) {
        if (previous == null || previous == 0) {
            return current != null && current > 0 ? 100.0 : 0.0;
        }
        if (current == null) {
            return -100.0;
        }
        double change = ((current - previous) / previous) * 100;
        return Math.round(change * 100.0) / 100.0;
    }
}