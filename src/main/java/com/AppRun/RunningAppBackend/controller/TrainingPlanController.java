package com.AppRun.RunningAppBackend.controller;

import com.AppRun.RunningAppBackend.entity.TrainingPlan;
import com.AppRun.RunningAppBackend.entity.User;
import com.AppRun.RunningAppBackend.repository.TrainingPlanRepository;
import com.AppRun.RunningAppBackend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/training-plans")
@CrossOrigin(origins = "*")
public class TrainingPlanController {

    private final TrainingPlanRepository planRepository;
    private final UserRepository userRepository;

    public TrainingPlanController(TrainingPlanRepository planRepository, UserRepository userRepository) {
        this.planRepository = planRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    // 🔹 Выбрать план (на 1 неделю)
    @PostMapping("/select")
    public ResponseEntity<?> selectPlan(@RequestParam String planType) {
        try {
            User user = getCurrentUser();

            // Деактивировать старые планы
            planRepository.findActiveByUserId(user.getId(), LocalDateTime.now())
                    .forEach(plan -> {
                        plan.setActive(false);
                        planRepository.save(plan);
                    });

            // Создать новый план
            TrainingPlan plan = new TrainingPlan();
            plan.setUser(user);
            plan.setPlanType(planType);
            plan.setStartDate(LocalDateTime.now());
            plan.setEndDate(LocalDateTime.now().plusWeeks(1));
            plan.setActive(true);

            planRepository.save(plan);

            return ResponseEntity.ok("План активирован на 1 неделю");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 🔹 Получить активный план
    @GetMapping("/active")
    public ResponseEntity<TrainingPlan> getActivePlan() {
        try {
            User user = getCurrentUser();
            TrainingPlan plan = planRepository.findFirstByUserIdAndIsActive(user.getId(), true)
                    .orElse(null);

            if (plan != null && plan.getEndDate().isBefore(LocalDateTime.now())) {
                plan.setActive(false);
                planRepository.save(plan);
                return ResponseEntity.ok(null);
            }

            return ResponseEntity.ok(plan);
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }
}