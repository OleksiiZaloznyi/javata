package com.example.javata.repository;

import java.time.LocalDate;
import java.util.List;
import com.example.javata.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByBirthDateBetween(LocalDate fromDate, LocalDate toDate);
}
