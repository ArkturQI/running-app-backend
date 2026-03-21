package com.AppRun.RunningAppBackend.util;

import org.springframework.stereotype.Component;

@Component
public class EmailValidator {

    // Regex для валидации email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    // Минимальная и максимальная длина
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 50;

    /**
     * Проверка email на корректность
     * @param email Email для проверки
     * @throws IllegalArgumentException если email некорректный
     */
    public void validate(String email) {
        // Проверка на null
        if (email == null) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }

        // Trim пробелов
        String trimmedEmail = email.trim();

        // Проверка на пустоту
        if (trimmedEmail.isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }

        // Проверка на пробелы внутри
        if (trimmedEmail.contains(" ")) {
            throw new IllegalArgumentException("Email не должен содержать пробелы");
        }

        // Проверка длины
        if (trimmedEmail.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Email слишком короткий (минимум " + MIN_LENGTH + " символов)");
        }

        if (trimmedEmail.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Email слишком длинный (максимум " + MAX_LENGTH + " символов)");
        }

        // Проверка формата через Regex
        if (!trimmedEmail.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Некорректный формат email (пример: user@gmail.com)");
        }

        // Проверка наличия @
        if (!trimmedEmail.contains("@")) {
            throw new IllegalArgumentException("Email должен содержать символ @");
        }

        // Проверка наличия домена после @
        String[] parts = trimmedEmail.split("@");
        if (parts.length != 2 || parts[1].isEmpty()) {
            throw new IllegalArgumentException("Email должен содержать домен (например, @gmail.com)");
        }
    }
}