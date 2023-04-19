package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreDbStorageTest {
    private final FilmStorage filmStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final GenreStorage genreStorage;

    @BeforeEach
    public void beforeEach() {

    }

    @Test
    public void batchAddFilmGenreTest() {
        List<Genre> genreList = List.of(
                Genre.builder().id(2).build(),
                Genre.builder().id(4).build());
        Film film = Film.builder()
                .name("1")
                .description("111")
                .releaseDate(LocalDate.of(2010, 12, 10))
                .duration(135)
                .mpa(Mpa.builder().id(5).build())
                .genres(genreList)
                .build();
        int newId = filmStorage.add(film);
        filmGenreStorage.batchAddFilmGenre(newId, genreList.stream().mapToInt(Genre::getId).toArray());

        assertEquals(genreList.size(), genreStorage.getFilmGenres(newId).size());
    }

    @Test
    public void removeFilmGenresTest() {
        List<Genre> genreList = List.of(
                Genre.builder().id(2).build(),
                Genre.builder().id(4).build());
        Film film = Film.builder()
                .name("1")
                .description("111")
                .releaseDate(LocalDate.of(2010, 12, 10))
                .duration(135)
                .mpa(Mpa.builder().id(5).build())
                .genres(genreList)
                .build();
        int newId = filmStorage.add(film);
        filmGenreStorage.batchAddFilmGenre(newId, genreList.stream().mapToInt(Genre::getId).toArray());

        filmGenreStorage.removeFilmGenre(newId);

        assertEquals(0, genreStorage.getFilmGenres(newId).size());
    }
}
