package com.AppRun.RunningAppBackend.repository;

import com.AppRun.RunningAppBackend.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    // Найти все тренировки пользователя
    List<Workout> findByUserId(Long userId);

    // Найти тренировки за период (для статистики и лидерборда)
    @Query("SELECT w FROM Workout w WHERE w.user.id = :userId AND w.startTime BETWEEN :start AND :end")
    List<Workout> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}