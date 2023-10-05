package com.example.javata.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import com.example.javata.model.User;

public class AgeValidator {
    public static boolean isValidAge(User user, int age) {
        LocalDate currentDate = LocalDate.now();
        return ChronoUnit.YEARS.between(user.getBirthDate(), currentDate) < age;
    }
}
