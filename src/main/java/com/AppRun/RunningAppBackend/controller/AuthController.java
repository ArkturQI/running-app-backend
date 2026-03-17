package com.AppRun.RunningAppBackend.controller;

import com.AppRun.RunningAppBackend.dto.LoginRequest;
import com.AppRun.RunningAppBackend.dto.RegisterRequest;
import com.AppRun.RunningAppBackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        System.out.println("🔍 [Controller] Регистрация: " + request.getEmail());

        try {
            var user = authService.register(request.getEmail(), request.getPassword());
            System.out.println("✅ [Controller] Пользователь создан: ID = " + user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Пользователь зарегистрирован");
            response.put("userId", user.getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("❌ [Controller] Ошибка регистрации: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        System.out.println("🔍 [Controller] Вход: " + request.getEmail());

        try {
            String token = authService.login(request.getEmail(), request.getPassword());

            System.out.println("✅ [Controller] Токен создан: " + token.substring(0, 30) + "...");

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Успешный вход");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ [Controller] Ошибка входа: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}