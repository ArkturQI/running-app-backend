package com.AppRun.RunningAppBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "workout_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    @JsonIgnore
    private Workout workout;

    @NotNull(message = "Широта обязательна")
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull(message = "Долгота обязательна")
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @NotNull(message = "Время обязательно")
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "altitude")
    private Double altitude;

    @Column(name = "accuracy")
    private Float accuracy;
}