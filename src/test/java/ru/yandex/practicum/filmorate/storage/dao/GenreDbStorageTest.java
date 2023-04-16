package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final GenreStorage genreStorage;
    private final FilmStorage filmStorage;

    @Test
    public void genreGetTest() {
        String firstGenreName = "Комедия";

        Genre genreFromDB = genreStorage.get(1);

        assertEquals(1, genreFromDB.getId());
        assertEquals(firstGenreName, genreFromDB.getName());
    }

    @Test
    public void genreWrongIdGetTest() {
        int nonExistIndex = -1;

        assertThrows(NotFoundException.class, () -> genreStorage.get(nonExistIndex));
    }

    @Test
    public void genreGetAllTest() {
        int genresCount = 6;

        int countGenresFromDB = genreStorage.getAll().size();

        assertEquals(genresCount, countGenresFromDB);
    }

    @Test
    public void getFilmGenresTest() {
        List<Genre> genreList = List.of(
                Genre.builder().id(2).build(),
                Genre.builder().id(4).build());
        Film film = filmStorage.add(Film.builder()
                .name("1")
                .description("111")
                .releaseDate(LocalDate.of(2010, 12, 10))
                .duration(135)
                .genres(genreList)
                .mpa(Mpa.builder().id(5).build())
                .build());

        List<Genre> listFromDB = genreStorage.getFilmGenres(film.getId());

        assertEquals(genreList.size(), listFromDB.size());
        assertEquals(genreList.get(1).getId(), listFromDB.get(1).getId());
    }

    @Test
    public void addFilmGenreTest() {
        List<Genre> genreList = List.of(
                Genre.builder().id(2).build(),
                Genre.builder().id(4).build());
        Film film = filmStorage.add(Film.builder()
                .name("1")
                .description("111")
                .releaseDate(LocalDate.of(2010, 12, 10))
                .duration(135)
                .genres(genreList)
                .mpa(Mpa.builder().id(5).build())
                .build());

        genreStorage.addFilmGenre(film.getId(), 6);
        List<Genre> listFromDB = genreStorage.getFilmGenres(film.getId());

        assertEquals(3, listFromDB.size());
        assertTrue(listFromDB.stream().mapToInt(Genre::getId).anyMatch(id -> id == 6));
    }

    @Test
    public void addFilmGenreDuplicateTest() {
        List<Genre> genreList = List.of(
                Genre.builder().id(2).build(),
                Genre.builder().id(4).build());
        Film film = filmStorage.add(Film.builder()
                .name("1")
                .description("111")
                .releaseDate(LocalDate.of(2010, 12, 10))
                .duration(135)
                .genres(genreList)
                .mpa(Mpa.builder().id(5).build())
                .build());

        genreStorage.addFilmGenre(film.getId(), 6);
        genreStorage.addFilmGenre(film.getId(), 6);
        List<Genre> listFromDB = genreStorage.getFilmGenres(film.getId());

        assertEquals(3, listFromDB.size());
        assertTrue(listFromDB.stream().mapToInt(Genre::getId).anyMatch(id -> id == 6));
    }

    @Test
    public void removeFilmGenreTest() {
        List<Genre> genreList = List.of(
                Genre.builder().id(2).build(),
                Genre.builder().id(4).build());
        Film film = filmStorage.add(Film.builder()
                .name("1")
                .description("111")
                .releaseDate(LocalDate.of(2010, 12, 10))
                .duration(135)
                .genres(genreList)
                .mpa(Mpa.builder().id(5).build())
                .build());

        genreStorage.removeFilmGenres(film.getId());
        List<Genre> listFromDB = genreStorage.getFilmGenres(film.getId());

        assertEquals(0, listFromDB.size());
    }
}
