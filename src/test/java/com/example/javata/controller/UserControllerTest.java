package com.example.javata.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.example.javata.model.User;
import com.example.javata.repository.UserRepository;
import com.example.javata.service.UserService;
import jakarta.annotation.Resource;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {
    @Resource
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    private User testUserValid;
    private String testUserJsonInvalidAge;
    private String testUserJsonInvalidEmail;
    private String testUserJsonValid;
    private String testUserJsonValidUpdated;

    @Before
    public void setUp() {
        testUserValid = new User(1L,
                "john@doe.com",
                "John",
                "Doe",
                LocalDate.of(2003,
                        01,
                        01));

        JSONObject jsonInvalidAge = new JSONObject();
        jsonInvalidAge.put("id", 1);
        jsonInvalidAge.put("email", "john@doe.com");
        jsonInvalidAge.put("firstName", "John");
        jsonInvalidAge.put("lastName", "Doe");
        jsonInvalidAge.put("birthDate", "2013-01-01");
        jsonInvalidAge.put("address", JSONObject.NULL);
        jsonInvalidAge.put("phoneNumber", JSONObject.NULL);
        testUserJsonInvalidAge = jsonInvalidAge.toString();

        JSONObject jsonInvalidEmail = new JSONObject();
        jsonInvalidEmail.put("id", 1);
        jsonInvalidEmail.put("email", "InvalidEmail");
        jsonInvalidEmail.put("firstName", "John");
        jsonInvalidEmail.put("lastName", "Doe");
        jsonInvalidEmail.put("birthDate", "2003-01-01");
        jsonInvalidEmail.put("address", JSONObject.NULL);
        jsonInvalidEmail.put("phoneNumber", JSONObject.NULL);
        testUserJsonInvalidEmail = jsonInvalidEmail.toString();

        JSONObject jsonValid = new JSONObject();
        jsonValid.put("id", 1);
        jsonValid.put("email", "john@doe.com");
        jsonValid.put("firstName", "John");
        jsonValid.put("lastName", "Doe");
        jsonValid.put("birthDate", "2003-01-01");
        jsonValid.put("address", JSONObject.NULL);
        jsonValid.put("phoneNumber", JSONObject.NULL);
        testUserJsonValid = jsonValid.toString();
    }

    @Test
    public void testA_CreateUser_isOk() throws Exception {
        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUserJsonValid))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.email")
                        .value("john@doe.com"));
    }

    @Test
    public void testB_CreateUser_Age_notOk() throws Exception {
        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUserJsonInvalidAge))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testC_CreateUser_Email_notOk() throws Exception {
        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUserJsonInvalidEmail))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testD_SearchUsersByBirthDateRange_isOk() throws Exception {
        List<User> usersInRange = new ArrayList<>();
        usersInRange.add(testUserValid);

        mockMvc.perform(get("/users/search")
                        .param("fromDate", "1990-01-01")
                        .param("toDate", "2010-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email")
                        .value("john@doe.com"));
    }

    @Test
    public void testE_UpdateUser_isOk() throws Exception {
        JSONObject jsonUpdated = new JSONObject();
        jsonUpdated.put("id", 1);
        jsonUpdated.put("email", "john@doe.com");
        jsonUpdated.put("firstName", "Johnson");
        jsonUpdated.put("lastName", "Doeson");
        jsonUpdated.put("birthDate", "2003-01-01");
        jsonUpdated.put("address", JSONObject.NULL);
        jsonUpdated.put("phoneNumber", JSONObject.NULL);
        testUserJsonValidUpdated = jsonUpdated.toString();

        mockMvc.perform(put("/users/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUserJsonValidUpdated))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.firstName")
                        .value("Johnson"));
    }

    @Test
    public void testF_UpdateUser_notFound_notOk() throws Exception {
        mockMvc.perform(put("/users/update/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testG_UpdateUser_invalidAge_notOk() throws Exception {
        JSONObject jsonUpdated = new JSONObject();
        jsonUpdated.put("id", 1);
        jsonUpdated.put("email", "john@doe.com");
        jsonUpdated.put("firstName", "John");
        jsonUpdated.put("lastName", "Doe");
        jsonUpdated.put("birthDate", "2013-01-01");
        jsonUpdated.put("address", JSONObject.NULL);
        jsonUpdated.put("phoneNumber", JSONObject.NULL);
        testUserJsonValidUpdated = jsonUpdated.toString();

        mockMvc.perform(put("/users/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUserJsonInvalidAge))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testH_UpdateUser_invalidEmail_notOk() throws Exception {
        JSONObject jsonUpdated = new JSONObject();
        jsonUpdated.put("id", 1);
        jsonUpdated.put("email", "InvalidEmail");
        jsonUpdated.put("firstName", "John");
        jsonUpdated.put("lastName", "Doe");
        jsonUpdated.put("birthDate", "2003-01-01");
        jsonUpdated.put("address", JSONObject.NULL);
        jsonUpdated.put("phoneNumber", JSONObject.NULL);
        testUserJsonValidUpdated = jsonUpdated.toString();

        mockMvc.perform(put("/users/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUserJsonInvalidEmail))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testI_DeleteUser_isOk() throws Exception {
        mockMvc.perform(delete("/users/delete/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testJ_DeleteUser_userNotFound_notOk() throws Exception {
        mockMvc.perform(delete("/users/delete/{id}", 1))
                .andExpect(status().isNotFound());
    }
}
