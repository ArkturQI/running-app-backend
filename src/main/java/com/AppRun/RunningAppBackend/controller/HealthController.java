package com.AppRun.RunningAppBackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String healthCheck(){
        return "{\"status\": \"OK\", \"message\": \"Running App Backend is alive! 🏃\"}";
    }
}
