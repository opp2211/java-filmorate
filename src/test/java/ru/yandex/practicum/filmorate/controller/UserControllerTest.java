package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @SneakyThrows
    @Test
    void addValidUser() {
        User user = User.builder()
                .email("example@yandex.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(1990, 8, 11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void addUserBlankName() {
        String login = "login1";
        User user = User.builder()
                .email("example@yandex.ru")
                .login("login1")
                .name(" ")
                .birthday(LocalDate.of(1990, 8, 11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(login));
    }

    @SneakyThrows
    @Test
    void addInvalidEmailUser() {
        User user = User.builder()
                .email("example#yandex.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(1990, 8, 11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void addBlankLoginUser() {
        User user = User.builder()
                .email("example@yandex.ru")
                .login(" ")
                .name("name1")
                .birthday(LocalDate.of(1990, 8, 11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void addUserLoginContainsSpaces() {
        User user = User.builder()
                .email("example@yandex.ru")
                .login("log in1")
                .name("name1")
                .birthday(LocalDate.of(1990, 8, 11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void addInvalidBirthdayUser() {
        User user = User.builder()
                .email("example@yandex.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(2024, 8, 11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
}
