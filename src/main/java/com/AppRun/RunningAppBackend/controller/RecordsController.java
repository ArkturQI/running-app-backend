package com.AppRun.RunningAppBackend.controller;

import com.AppRun.RunningAppBackend.entity.PersonalRecord;
import com.AppRun.RunningAppBackend.entity.User;
import com.AppRun.RunningAppBackend.repository.PersonalRecordRepository;
import com.AppRun.RunningAppBackend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@CrossOrigin(origins = "*")
public class RecordsController {

    private final PersonalRecordRepository recordRepository;
    private final UserRepository userRepository;

    public RecordsController(PersonalRecordRepository recordRepository,
                             UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    // 🔹 Получить все рекорды пользователя
    @GetMapping
    public ResponseEntity<List<PersonalRecord>> getMyRecords() {
        try {
            User user = getCurrentUser();
            List<PersonalRecord> records = recordRepository.findByUserId(user.getId());
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    // 🔹 Обновить рекорд (вызывается после завершения тренировки)
    @PostMapping("/update")
    public ResponseEntity<?> updateRecord(
            @RequestParam Double distanceKm,
            @RequestParam Long timeSeconds,
            @RequestParam Long workoutId) {
        try {
            User user = getCurrentUser();

            // Стандартные дистанции для рекордов
            double[] standardDistances = {0.4, 1.0, 2.0, 1.609, 5.0, 10.0, 21.1, 42.2};
            String[] distanceNames = {"400м", "1км", "2км", "Миля", "5К", "10К", "Полумарафон", "Марафон"};

            // Найти ближайшую стандартную дистанцию
            for (int i = 0; i < standardDistances.length; i++) {
                if (Math.abs(distanceKm - standardDistances[i]) < 0.5) {
                    // Проверить есть ли существующий рекорд
                    PersonalRecord existingRecord = recordRepository
                            .findByUserIdAndDistance(user.getId(), standardDistances[i])
                            .orElse(null);

                    if (existingRecord == null || timeSeconds < existingRecord.getBestTimeSeconds()) {
                        // Новый рекорд!
                        PersonalRecord record = existingRecord != null ? existingRecord : new PersonalRecord();
                        record.setUser(user);
                        record.setDistanceKm(standardDistances[i]);
                        record.setDistanceName(distanceNames[i]);
                        record.setBestTimeSeconds(timeSeconds);
                        record.setAchievedAt(LocalDateTime.now());
                        record.setWorkoutId(workoutId);
                        record.setUpdatedAt(LocalDateTime.now());

                        recordRepository.save(record);

                        return ResponseEntity.ok("Новый рекорд на " + distanceNames[i] + "!");
                    }
                    break;
                }
            }

            return ResponseEntity.ok("Рекорд не обновлён");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 🔹 Получить рекорд по дистанции
    @GetMapping("/distance/{distanceName}")
    public ResponseEntity<PersonalRecord> getRecordByDistance(@PathVariable String distanceName) {
        try {
            User user = getCurrentUser();
            double distance = parseDistanceName(distanceName);

            PersonalRecord record = recordRepository
                    .findByUserIdAndDistance(user.getId(), distance)
                    .orElse(null);

            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }

    private double parseDistanceName(String name) {
        switch (name) {
            case "400м": return 0.4;
            case "1км": return 1.0;
            case "2км": return 2.0;
            case "Миля": return 1.609;
            case "5К": return 5.0;
            case "10К": return 10.0;
            case "Полумарафон": return 21.1;
            case "Марафон": return 42.2;
            default: return 0.0;
        }
    }
}