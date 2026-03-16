package com.AppRun.RunningAppBackend.config;

import com.AppRun.RunningAppBackend.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/users").permitAll()

                        // 🔐 ЗАЩИЩЁННЫЕ ЭНДПОИНТЫ (явно для каждого метода)
                        .requestMatchers(HttpMethod.GET, "/api/workouts/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/workouts/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/workouts/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/workouts/**").authenticated()

                        // 🔐 Всё остальное в /api/* требует авторизации
                        .requestMatchers("/api/**").authenticated()

                        // 🔐 Всё остальное требует авторизации
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}