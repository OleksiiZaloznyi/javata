package com.example.javata.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.example.javata.exceptions.AgeValidationException;
import com.example.javata.model.User;
import com.example.javata.service.UserService;
import org.json.JSONObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User testUser;

    private String testUserJson;

    @Before
    public void setUp() {
        testUser = new User(1L,
                "john@doe.com",
                "John",
                "Doe",
                LocalDate.of(2013,
                        01,
                        01));


        JSONObject json = new JSONObject();
        json.put("id", 1);
        json.put("email", "john@doe.com");
        json.put("firstName", "John");
        json.put("lastName", "Doe");
        json.put("birthDate", "2013-01-01");
        json.put("address", JSONObject.NULL);
        json.put("phoneNumber", JSONObject.NULL);

        testUserJson = json.toString();
    }

    @Test
    public void testCreateUser_isOk() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(ResponseEntity.ok(testUser));

        mockMvc.perform(post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUserJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.email")
                        .value("john@doe.com"));
    }

    @Test
    public void testCreateUser_ValidAge() throws Exception {
        // Создаем пользователя, который старше 18 лет
        User testUser = new User();
        testUser.setEmail("john@doe.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setBirthDate(LocalDate.of(2000, 1, 1)); // Пользователь старше 18 лет

        // Отправляем POST-запрос для создания пользователя
        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUserJson))
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Ожидаем код 201 Created
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@doe.com")); // Проверяем другие поля, если необходимо
    }

    @Test
    public void testCreateUser_notOk() throws Exception {
        mockMvc.perform(post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AgeValidationException))
                .andExpect(result -> assertEquals("User must be older than 18 years",
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void testUpdateUser() throws Exception {
        when(userService.updateUser(any(Long.class), any(User.class))).thenReturn(ResponseEntity.ok(testUser));

        mockMvc.perform(put("/users/update/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@doe.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/delete/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchUsersByBirthDateRange() throws Exception {
        List<User> usersInRange = new ArrayList<>();
        usersInRange.add(testUser);

        when(userService
                .searchUserByBirthDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(ResponseEntity.ok(usersInRange));

        mockMvc.perform(get("/users/search")
                .param("fromDate", "1990-01-01")
                .param("toDate", "2000-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john@doe.com"));
    }

}