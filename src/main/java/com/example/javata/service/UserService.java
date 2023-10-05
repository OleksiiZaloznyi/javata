package com.example.javata.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import com.example.javata.model.User;
import com.example.javata.repository.UserRepository;
import static com.example.javata.validator.AgeValidator.isValidAge;
import static com.example.javata.validator.EmailValidator.isValidEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Value("${minimum.age}")
    private int minAge;

    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        if (isValidAge(user, minAge)) {
            log.error("User must be older than 18 years");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (!isValidEmail(user.getEmail())) {
            log.error("Email is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        User createdUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = optionalUser.get();

        if (isValidAge(updatedUser, minAge)) {
            log.error("User must be older than 18 years");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (!isValidEmail(updatedUser.getEmail())) {
            log.error("Email is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setBirthDate(updatedUser.getBirthDate());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<User>> searchUserByBirthDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.badRequest().build();
        }
        List<User> users = userRepository.findByBirthDateBetween(fromDate, toDate);
        return ResponseEntity.ok(users);
    }
}
