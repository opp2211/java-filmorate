package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FilmService filmService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public User addNew(@RequestBody @Valid User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug(user.toString());
        return userService.add(user);
    }

    @PutMapping
    public User updateExistUserData(@RequestBody @Valid User user) {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") int userId) {
        return userService.get(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable int userId) {
        userService.delete(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        userService.addFriendToUser(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        userService.removeUserFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") int userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable("id") int userId, @PathVariable int otherId) {
        return userService.getMutualFriends(userId, otherId);
    }

    @GetMapping("/{id}/recommendations")
    public List<Film> getUsersRecommendations(@PathVariable("id") int userId) {
        return filmService.getUsersRecommendations(userId);
    }
}
