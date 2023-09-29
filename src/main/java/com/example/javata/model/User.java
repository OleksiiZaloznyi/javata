package com.example.javata.model;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name length must be between 1 and 50 characters")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name length must be between 1 and 50 characters")
    private String lastName;
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
