package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public User addNew(@RequestBody User user) {
        log.debug("Получен запрос на добавление пользователя:\n" + user.toString());
        if(!validation(user))
            throw new ValidationException();
        users.put(idGenerator, user);
        user.setId(idGenerator++);
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        log.debug("Пользователь успешно добавлен:\n" + user);
        return user;
    }
    @PutMapping
    public User updateExistUserData(@RequestBody User user) {
        log.debug("Получен запрос на изменение данных пользователя:\n" + user.toString());
        if (users.containsKey(user.getId())){
            if (!validation(user))
                throw new ValidationException();
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

    private boolean validation(User user) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("Ошибка валидации: электронная почта не может быть пустой и должна содержать символ '@' " +
                    "-- email=" + user.getEmail());
            return false;
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("Ошибка валидации: логин не может быть пустым и содержать пробелы " +
                    "-- login=\"" + user.getLogin() + "\"");
            return false;
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Ошибка валидации: дата рождения не может быть в будущем " +
                    "-- birthday=" + user.getBirthday().format(formatter));
            return false;
        }

        return true;
    }
}
