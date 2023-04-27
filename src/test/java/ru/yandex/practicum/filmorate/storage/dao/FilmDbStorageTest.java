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
        film1 = Film.builder()
                .name("1")
                .description("111")
                .releaseDate(LocalDate.of(2010, 12, 10))
                .duration(135)
                .genres(genreList1)
                .mpa(Mpa.builder().id(5).build())
                .build();
        int film1Id = filmStorage.add(film1);
        int film2Id = filmStorage.add(film1);
        int film3Id = filmStorage.add(film1);
        film1.setId(film1Id);
        film2 = film1.toBuilder()
                .id(film2Id)
                .build();
        film2 = film1.toBuilder()
                .id(film3Id)
                .build();
    }

    @AfterEach
    public void afterEach() {
        userStorage.removeAll();
        filmStorage.removeAll();
    }

    @Test
    public void addTest() {
        int initialSize = filmStorage.getAll().size();

        Film film = Film.builder()
                .name("1")
                .description("111")
                .releaseDate(LocalDate.of(2010, 12, 10))
                .duration(135)
                .mpa(Mpa.builder().id(5).build())
                .build();
        int newId = filmStorage.add(film);


        assertEquals(initialSize + 1, filmStorage.getAll().size());
        assertNotEquals(newId, film.getId());
    }

    @Test
    public void removeTest() {
        int initialSize = filmStorage.getAll().size();

        filmStorage.delete(film1.getId());

        assertEquals(initialSize - 1, filmStorage.getAll().size());
        assertEquals(film2.getId(), filmStorage.get(film2.getId()).getId());
    }

    @Test
    public void updateTest() {
        Film film = Film.builder()
                .id(film1.getId())
                .name("2")
                .description("11122")
                .releaseDate(LocalDate.of(2008, 8, 26))
                .duration(180)
                .mpa(Mpa.builder().id(1).build())
                .build();
        filmStorage.update(film);

        assertEquals(film.getId(), filmStorage.get(film1.getId()).getId());
        assertEquals(film.getName(), filmStorage.get(film1.getId()).getName());
        assertEquals(film.getDescription(), filmStorage.get(film1.getId()).getDescription());
        assertEquals(film.getReleaseDate(), filmStorage.get(film1.getId()).getReleaseDate());
        assertEquals(film.getDuration(), filmStorage.get(film1.getId()).getDuration());
        assertEquals(film.getMpa(), filmStorage.get(film1.getId()).getMpa());
    }
}
