package com.AppRun.RunningAppBackend.controller;

import com.AppRun.RunningAppBackend.dto.LeaderboardEntry;
import com.AppRun.RunningAppBackend.service.LeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/social")
@CrossOrigin(origins = "*")
public class SocialController {

    private final LeaderboardService leaderboardService;

    public SocialController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }
    
    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard(
            @RequestParam(defaultValue = "week") String period) {
        try {
            List<LeaderboardEntry> leaderboard = leaderboardService.getLeaderboard(period);
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
