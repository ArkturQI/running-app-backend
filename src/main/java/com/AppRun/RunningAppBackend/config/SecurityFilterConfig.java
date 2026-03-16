package com.AppRun.RunningAppBackend.config;

import com.AppRun.RunningAppBackend.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityFilterConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 🔓 ПУБЛИЧНЫЕ ЭНДПОИНТЫ (без токена)
                        .requestMatchers("/auth/**").permitAll()      // Регистрация и вход
                        .requestMatchers("/api/users").permitAll()    // ✅ Создание пользователя (POST)

                        // 🔐 ЗАЩИЩЁННЫЕ ЭНДПОИНТЫ (нужен токен)
                        .requestMatchers("/api/**").authenticated()   // Всё остальное в /api/*
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}