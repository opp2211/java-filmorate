package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @SneakyThrows
    @Test
    void addValidFilm() {
        Film film = Film.builder()
                .name("film1")
                .description("filmDescription1")
                .duration(100)
                .releaseDate(LocalDate.of(2014, 5, 12))
                .genres(Collections.emptyList())
                .mpa(Mpa.builder().id(2).build())
                .build();

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(film)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void addBlankNameFilm() {
        Film film = Film.builder()
                .name(" ")
                .description("filmDescription1")
                .duration(100)
                .releaseDate(LocalDate.of(2014, 5, 12))
                .build();

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void addExceededDescriptionFilm() {
        Film film = Film.builder()
                .name("film1")
                .description("A".repeat(201))
                .duration(100)
                .releaseDate(LocalDate.of(2014, 5, 12))
                .build();

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void addInvalidReleaseDateFilm() {
        Film film = Film.builder()
                .name("film1")
                .description("filmDescription1")
                .duration(100)
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build();

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void addNegativeDurationFilm() {
        Film film = Film.builder()
                .name("film1")
                .description("filmDescription1")
                .duration(-100)
                .releaseDate(LocalDate.of(2014, 5, 12))
                .build();

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }
}
