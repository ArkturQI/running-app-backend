package com.AppRun.RunningAppBackend.controller;

import com.AppRun.RunningAppBackend.dto.StatsResponse;
import com.AppRun.RunningAppBackend.service.StatsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    // 🔹 Статистика за неделю
    @GetMapping("/weekly")
    public ResponseEntity<StatsResponse> getWeeklyStats(
            @RequestParam(required = false) Integer week,
            @RequestParam(required = false) Integer year) {
        try {
            StatsResponse stats = statsService.getWeeklyStats(week, year);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 🔹 Статистика за месяц
    @GetMapping("/monthly")
    public ResponseEntity<StatsResponse> getMonthlyStats(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        try {
            StatsResponse stats = statsService.getMonthlyStats(month, year);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 🔹 Статистика за год
    @GetMapping("/yearly")
    public ResponseEntity<StatsResponse> getYearlyStats(
            @RequestParam(required = false) Integer year) {
        try {
            StatsResponse stats = statsService.getYearlyStats(year);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 🔹 Сравнение периодов
    @GetMapping("/compare")
    public ResponseEntity<StatsResponse> comparePeriods(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start1,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end1,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start2,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end2) {
        try {
            StatsResponse stats = statsService.comparePeriods(start1, end1, start2, end2);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}