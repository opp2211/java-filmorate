package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
    @PostMapping
    public User addNew(@RequestBody @Valid User user) {
        log.debug("Получен запрос на добавление пользователя:\n" + user.toString());
        users.put(idGenerator, user);
        user.setId(idGenerator++);
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        log.debug("Пользователь успешно добавлен:\n" + user);
        return user;
    }
    @PutMapping
    public User updateExistUserData(@RequestBody @Valid User user) {
        log.debug("Получен запрос на изменение данных пользователя:\n" + user.toString());
        if (users.containsKey(user.getId())){
            users.put(user.getId(), user);
            if (user.getName().isBlank())
                user.setName(user.getLogin());
        } else {
            log.debug("Ошибка: пользователь с ID=" + user.getId() + " не найден");
            throw new NotFoundException();
        }
        log.debug("Данные пользователя успешно изменены:\n" + user);
        return user;
    }
}
