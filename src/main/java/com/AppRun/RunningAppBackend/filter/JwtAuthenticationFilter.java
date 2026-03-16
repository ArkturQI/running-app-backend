package com.AppRun.RunningAppBackend.filter;

import com.AppRun.RunningAppBackend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 🔹 ЛОГ ДЛЯ ОТЛАДКИ
        System.out.println("🔍 [JWT Filter] Authorization header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);  // ✅ Убираем "Bearer " (7 символов)

            // 🔹 ЛОГ ДЛЯ ОТЛАДКИ
            System.out.println("🔍 [JWT Filter] Token found, length: " + token.length());

            try {
                Long userId = jwtUtil.validateTokenAndGetUserId(token);

                // 🔹 ЛОГ ДЛЯ ОТЛАДКИ
                System.out.println("✅ [JWT Filter] Token valid! User ID: " + userId);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                request.setAttribute("userId", userId);

            } catch (Exception e) {
                // 🔹 ЛОГ ОБ ОШИБКЕ
                System.out.println("❌ [JWT Filter] Token validation FAILED: " + e.getMessage());
                e.printStackTrace();
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}