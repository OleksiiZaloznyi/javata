package com.example.javata.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.example.javata.model.User;
import com.example.javata.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Before
    public void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("john@doe.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setBirthDate(LocalDate.of(1990,1,1));
    }

    @Test
    public void testCreateUser() throws Exception {
        String json = "{\"id\":\"1\",\"email\":\"john@doe.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthDate\":\"1990-01-01\",\"address\":\"null\",\"phoneNumber\":\"null\"}";
        when(userService.createUser(any(User.class))).thenReturn(ResponseEntity.ok(testUser));

        mockMvc.perform(post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.email")
                        .value("john@doe.com"));
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