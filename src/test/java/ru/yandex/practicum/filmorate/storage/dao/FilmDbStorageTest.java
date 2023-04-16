package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private Film film1;
    private Film film2;
    private Film film3;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void beforeEach() {
        User user = User.builder()
                .name("1")
                .login("123")
                .email("fda@asdf.ry")
                .birthday(LocalDate.of(1880, 6, 17))
                .build();
        user1 = userStorage.add(user);
        user2 = userStorage.add(user);
        user3 = userStorage.add(user);

        List<Genre> genreList1 = List.of(
                Genre.builder().id(2).build(),
                Genre.builder().id(4).build());
        film1 = filmStorage.add(Film.builder()
                .name("1")
                .description("111")
                .releaseDate(LocalDate.of(2010, 12, 10))
                .duration(135)
                .genres(genreList1)
                .mpa(Mpa.builder().id(5).build())
                .build());
        film2 = filmStorage.add(film1);
        film3 = filmStorage.add(film1);
    }

    @AfterEach
    public void afterEach() {
        userStorage.removeAll();
        filmStorage.removeAll();
    }

    @Test
    public void addTest() {
        int initialSize = filmStorage.getAll().size();
        List<Genre> genreList = List.of(
                Genre.builder().id(2).build(),
                Genre.builder().id(4).build());
        Film film1 = filmStorage.add(Film.builder()
                .name("1")
                .description("111")
                .releaseDate(LocalDate.of(2010, 12, 10))
                .duration(135)
                .genres(genreList)
                .mpa(Mpa.builder().id(5).build())
                .build());

        Film film2 = filmStorage.add(film1);
        filmStorage.add(film1);

        assertEquals(initialSize + 3, filmStorage.getAll().size());
        assertNotEquals(film1.getId(), film2.getId());
    }

    @Test
    public void removeTest() {
        int initialSize = filmStorage.getAll().size();

        filmStorage.remove(film1.getId());

        assertEquals(initialSize - 1, filmStorage.getAll().size());
        assertEquals(film2.getId(), filmStorage.get(film2.getId()).getId());
    }

    @Test
    public void updateTest() {
        List<Genre> genreList2 = List.of(
                Genre.builder().id(3).build());
        Film film = filmStorage.update(Film.builder()
                .id(film1.getId())
                .name("2")
                .description("11122")
                .releaseDate(LocalDate.of(2008, 8, 26))
                .duration(180)
                .genres(genreList2)
                .mpa(Mpa.builder().id(1).build())
                .build());

        assertEquals(film.getId(), filmStorage.get(film1.getId()).getId());
        assertEquals(film.getName(), filmStorage.get(film1.getId()).getName());
        assertEquals(film.getDescription(), filmStorage.get(film1.getId()).getDescription());
        assertEquals(film.getReleaseDate(), filmStorage.get(film1.getId()).getReleaseDate());
        assertEquals(film.getDuration(), filmStorage.get(film1.getId()).getDuration());
        assertEquals(film.getGenres(), filmStorage.get(film1.getId()).getGenres());
        assertEquals(film.getMpa(), filmStorage.get(film1.getId()).getMpa());
    }

    @Test
    public void popularityTest() {
        filmStorage.addLike(film2.getId(), user1.getId());
        filmStorage.addLike(film1.getId(), user2.getId());
        filmStorage.addLike(film2.getId(), user3.getId());

        assertEquals(film2.getId(), filmStorage.getMostPopulars(1).get(0).getId());
    }
}
