package com.AppRun.RunningAppBackend.config;

import com.AppRun.RunningAppBackend.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 🔓 ПУБЛИЧНЫЕ ЭНДПОИНТЫ (без авторизации)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/users").permitAll()
                        .requestMatchers("/api/health").permitAll()
                        .requestMatchers("/").permitAll()

                        // ← ДОБАВЛЕНО: Лидерборд публичный
                        .requestMatchers("/api/social/leaderboard").permitAll()

                        // 🔐 ТРЕБУЮТ АВТОРИЗАЦИИ
                        .requestMatchers("/api/workouts/**").authenticated()
                        .requestMatchers("/api/workout-points/**").authenticated()
                        .requestMatchers("/api/stats/**").authenticated()
                        .requestMatchers("/api/users/**").authenticated()
                        .requestMatchers("/api/social/**").authenticated()
                        .requestMatchers("/api/training-plans/**").authenticated()
                        .requestMatchers("/api/records/**").authenticated()
                        .requestMatchers("/api/notifications/**").authenticated()

                        // 🔐 Всё остальное
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}