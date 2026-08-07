package com.AppRun.RunningAppBackend.util;

import org.springframework.stereotype.Component;

@Component
public class EmailValidator {

    // Regex для валидации email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 50;

    public void validate(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }

        String trimmedEmail = email.trim();

        if (trimmedEmail.isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }

        if (trimmedEmail.contains(" ")) {
            throw new IllegalArgumentException("Email не должен содержать пробелы");
        }

        if (trimmedEmail.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Email слишком короткий (минимум " + MIN_LENGTH + " символов)");
        }

        if (trimmedEmail.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Email слишком длинный (максимум " + MAX_LENGTH + " символов)");
        }

        if (!trimmedEmail.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Некорректный формат email (пример: user@gmail.com)");
        }

        if (!trimmedEmail.contains("@")) {
            throw new IllegalArgumentException("Email должен содержать символ @");
        }

        String[] parts = trimmedEmail.split("@");
        if (parts.length != 2 || parts[1].isEmpty()) {
            throw new IllegalArgumentException("Email должен содержать домен (например, @gmail.com)");
        }
    }
}
