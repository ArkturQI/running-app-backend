package com.AppRun.RunningAppBackend.util;

import com.AppRun.RunningAppBackend.entity.User;
import com.AppRun.RunningAppBackend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserUtil {

    private final UserRepository userRepository;

    public CurrentUserUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔹 Получить email текущего пользователя из токена
    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new RuntimeException("Пользователь не аутентифицирован");
        }

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userDetails.getUsername();  // ← Это email из токена
    }

    // 🔹 Получить объект пользователя
    public User getCurrentUser() {
        String email = getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + email));
    }

    // 🔹 Получить ID текущего пользователя
    public Long getCurrentUserId() {
        User user = getCurrentUser();
        return user.getId();
    }
}