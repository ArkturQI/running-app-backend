package com.AppRun.RunningAppBackend.repository;

import com.AppRun.RunningAppBackend.entity.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {

    @Query("SELECT tp FROM TrainingPlan tp WHERE tp.user.id = :userId AND tp.isActive = true AND tp.endDate > :now")
    List<TrainingPlan> findActiveByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    Optional<TrainingPlan> findFirstByUserIdAndIsActive(Long userId, boolean isActive);

    void deleteByEndDateBefore(LocalDateTime endDate);
}