package com.AppRun.RunningAppBackend.service;

import com.AppRun.RunningAppBackend.entity.User;
import com.AppRun.RunningAppBackend.repository.UserRepository;
import com.AppRun.RunningAppBackend.util.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final EmailValidator emailValidator; 

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       CustomUserDetailsService userDetailsService,
                       EmailValidator emailValidator) { 
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.emailValidator = emailValidator;  
    }

   
    public User register(String email, String plainPassword) {
        System.out.println("[AuthService] Регистрация: " + email);

     
        emailValidator.validate(email); 

     
        if (userRepository.existsByEmail(email)) {
            System.out.println("[AuthService] Email уже занят: " + email);
            throw new RuntimeException("Email уже занят: " + email);
        }

    
        if (plainPassword == null || plainPassword.length() < 6) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 6 символов");
        }

        User user = new User();
        user.setEmail(email.trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(plainPassword));

        User saved = userRepository.save(user);
        System.out.println("[AuthService] Пользователь создан: ID = " + saved.getId());

        return saved;
    }

    // Вход пользователя (возвращает токен)
    public String login(String email, String plainPassword) {
        System.out.println("[AuthService] Попытка входа: " + email);

        
        emailValidator.validate(email);  

        String normalizedEmail = email.trim().toLowerCase();
        System.out.println("[AuthService] Нормализованный email: " + normalizedEmail);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> {
                    System.out.println("[AuthService] Пользователь НЕ НАЙДЕН: " + normalizedEmail);
                    return new RuntimeException("Пользователь не найден");
                });

        System.out.println("[AuthService] Пользователь найден: ID = " + user.getId());

        boolean passwordMatches = passwordEncoder.matches(plainPassword, user.getPassword());
        System.out.println("[AuthService] Пароль совпадает: " + passwordMatches);

        if (!passwordMatches) {
            System.out.println("[AuthService] Неверный пароль");
            throw new RuntimeException("Неверный пароль");
        }

        System.out.println("[AuthService] Пароль верный");

        UserDetails userDetails = userDetailsService.loadUserByUsername(normalizedEmail);
        String token = jwtService.generateToken(userDetails);

        System.out.println("[AuthService] Токен создан: " + token.substring(0, 30) + "...");

        return token;
    }
}
