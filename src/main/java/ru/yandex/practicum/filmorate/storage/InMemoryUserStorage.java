package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> usersMap = new HashMap<>();
    private int idGenerator = 0;

    @Override
    public User add(@Valid User user) {
        int newId = idGenerator++;
        usersMap.put(newId, user);
        user.setId(newId);
        return user;
    }

    @Override
    public User remove(int id) {
        if (!usersMap.containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь с ID=%d не найден", id));
        }
        return usersMap.remove(id);
    }

    @Override
    public User update(@Valid User user) {
        int id = user.getId();
        if (!usersMap.containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь с ID=%d не найден", id));
        }
        return usersMap.put(id, user);
    }
}
