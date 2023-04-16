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

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

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
        int initialSize = userStorage.getAll().size();

        userStorage.add(user1);
        userStorage.add(user1);

        assertEquals(initialSize + 2, userStorage.getAll().size());
        assertEquals(1, userStorage.getAll().stream()
                .mapToInt(User::getId)
                .filter(id -> id == user1.getId()).count());
    }

    @Test
    public void removeTest() {
        int initialSize = userStorage.getAll().size();

        userStorage.remove(user1.getId());

        assertEquals(initialSize - 1, userStorage.getAll().size());
        assertEquals(0, userStorage.getAll().stream()
                .mapToInt(User::getId)
                .filter(id -> id == user1.getId()).count());
    }

    @Test
    public void removeAllTest() {
        userStorage.removeAll();

        assertEquals(0, userStorage.getAll().size());
    }

    @Test
    public void updateTest() {
        User user = User.builder()
                .id(user1.getId())
                .name("2222")
                .login("122223")
                .email("f222da@asdf.ry")
                .birthday(LocalDate.of(1780, 10, 17))
                .build();

        userStorage.update(user);

        assertEquals(user.getId(), userStorage.get(user.getId()).getId());
        assertEquals(user.getName(), userStorage.get(user.getId()).getName());
        assertEquals(user.getLogin(), userStorage.get(user.getId()).getLogin());
        assertEquals(user.getBirthday(), userStorage.get(user.getId()).getBirthday());
        assertEquals(user.getEmail(), userStorage.get(user.getId()).getEmail());
    }

    @Test
    public void getTest() {
        User user = User.builder()
                .id(user1.getId())
                .name("2222")
                .login("122223")
                .email("f222da@asdf.ry")
                .birthday(LocalDate.of(1980, 6, 15))
                .build();

        userStorage.add(user);

        assertEquals(user.getId(), userStorage.get(user.getId()).getId());
        assertEquals(user.getName(), userStorage.get(user.getId()).getName());
        assertEquals(user.getLogin(), userStorage.get(user.getId()).getLogin());
        assertEquals(user.getBirthday(), userStorage.get(user.getId()).getBirthday());
        assertEquals(user.getEmail(), userStorage.get(user.getId()).getEmail());
    }

    @Test
    public void addFriendTest() {
        int initialCount = userStorage.getFriends(user1.getId()).size();
        userStorage.addFriend(user1.getId(), user2.getId());

        assertEquals(initialCount + 1, userStorage.getFriends(user1.getId()).size());
    }

    @Test
    public void removeFriendTest() {
        int initialCount = userStorage.getFriends(user1.getId()).size();
        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.removeFriend(user1.getId(), user2.getId());

        assertEquals(initialCount, userStorage.getFriends(user1.getId()).size());
    }

    @Test
    public void getMutualFriendsTest() {
        int initialCount = userStorage.getMutualFriends(user1.getId(), user2.getId()).size();
        userStorage.addFriend(user1.getId(), user3.getId());
        userStorage.addFriend(user2.getId(), user3.getId());

        assertEquals(initialCount + 1, userStorage.getMutualFriends(user1.getId(), user2.getId()).size());
    }

}
