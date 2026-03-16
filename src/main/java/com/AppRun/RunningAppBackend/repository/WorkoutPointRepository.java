package com.AppRun.RunningAppBackend.repository;

import com.AppRun.RunningAppBackend.entity.WorkoutPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkoutPointRepository extends JpaRepository<WorkoutPoint, Long> {

    List<WorkoutPoint> findByWorkoutId(Long workoutId);

    void deleteByWorkoutId(Long workoutId);
}