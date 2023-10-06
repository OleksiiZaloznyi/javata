package com.example.javata.model;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @NonNull
    private String email;
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name length must be between 1 and 50 characters")
    @NonNull
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name length must be between 1 and 50 characters")
    @NonNull
    private String lastName;
    @Past(message = "Birth date must be in the past")
    @NonNull
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
